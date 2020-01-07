#!/usr/bin/env python3
"""
Create client certificates

Whenever we need to create a new client certificate (eg. A new client signs up) this script will create
the certificates and upload it to the database.

This script executes a docker container kylemanna/openvpn therefore the user that runs it needs to be in the docker group.

The following environment variables customizes the behaviour of the scripts:
 - CA_PASSWORD, the password for the CA certificate.
 - VPN_OPENVPN_CONTAINER, the container that has openvpn operational.

"""

import greenstalk
import logging
import os
import json

from .components.vpnapi import TheVPNCompanyClient

from .components.utils import Command, execute

log = logging.getLogger(__name__)

VPN_API_URL = os.getenv('VPN_API_URL', "http://localhost:8111")
log.debug("VPN API SERVER: %s" % VPN_API_URL)

VPN_QUEUE_SERVER = os.getenv('VPN_QUEUE_SERVER', "127.0.0.1")
log.debug("Queue server: %s" % VPN_QUEUE_SERVER)

VPN_QUEUE_PORT = os.getenv('VPN_QUEUE_PORT', 11300)
log.debug("Queue port: %s" % VPN_QUEUE_PORT)

VPN_QUEUE_TUBE = os.getenv('VPN_QUEUE_TUBE', "tube_cert_create")
log.debug("Queue tube: %s" % VPN_QUEUE_TUBE)

CA_PASSWORD = os.getenv('CA_PASSWORD')
log.debug("Password: %s" % CA_PASSWORD)

VPN_OPENVPN_CONTAINER = os.getenv('VPN_OPENVPN_CONTAINER', "kylemanna/openvpn:2.4")
log.debug("Container Name: %s" % VPN_OPENVPN_CONTAINER)

TVC_API_KEY = os.getenv('TVC_API_KEY')
log.debug("The VPN Company API Key: %s" % TVC_API_KEY)

ENV = os.getenv('ENV')
log.debug("Environment ENV: %s" % ENV)

if ENV is None or (ENV != "dev" and ENV != "prod" and ENV != "tests"):
    log.error("You need to configure the ENV environment variable. Possible values: 'dev' for development and 'prod' "
              "for production.")
    exit(1)

if TVC_API_KEY is None:
    log.error("A password for the TheVPNCompany API (TVC_API_KEY) needs to be defined  ")
    exit(1)

# set logging level according to selected environment
if ENV == "prod" or ENV == "tests":
    logging.basicConfig(level=logging.WARN)
else:
    logging.basicConfig(level=logging.DEBUG)

if CA_PASSWORD is None:
    log.error("Unable to get the credentials for the OpenCPN CA. Please define the environment variable CA_PASSWORD.")
    exit(1)

# TODO make this folder configurable
log.debug("Checking that folder /var/thevpncompany/data/conf exists.")
if not os.path.exists("/var/thevpncompany/data/conf"):
    log.error("OpenVPN Server Configuration with the private key is expected at /var/thevpncompany/data/conf. This "
              "folder has not been found.")
    exit(1)


# ensure that the image is installed locally and that the user can execute docker
command = execute("docker pull " + VPN_OPENVPN_CONTAINER)

if command.rc != 0:
    exit(1)


def main():
    """ Main process to loop on the queue """

    log.debug("Starting Server ...")

    try:
        with greenstalk.Client(host=VPN_QUEUE_SERVER, port=VPN_QUEUE_PORT, watch=VPN_QUEUE_TUBE,
                               use=VPN_QUEUE_TUBE) as queue:

            log.info("Daemon Ready")

            # import random
            # queue.put(body='{"common_name": "email@domain.com' + str(random.randint(1, 10000)) + '"}', ttr=3600)

            while True:
                log.debug("Waiting for job ... ")
                job = queue.reserve()
                if job is not None:
                    log.debug("[%s] New job received. Data: %s" % (job.id, job.body))

                    json_body = json.loads(job.body)
                    try:
                        result = process_single_certificate(json_body)
                        if result:
                            log.debug("Job processed successfully")
                            queue.delete(job)
                        else:
                            log.warning("Job finalized with error, burying job for analysis")
                            queue.bury(job)
                    except Exception as e:
                        log.error("A major error has occurred. Script cannot process job: %s" % json_body, exc_info=True)
                        queue.bury(job)
    except ConnectionRefusedError:
        log.error(
            "Unable to connect to ip %s:%s. Is beanstalkd the daemon running?" % (VPN_QUEUE_SERVER, VPN_QUEUE_PORT),
            exc_info=True)


def process_single_certificate(request) -> bool:
    """
        Process the creation of a single server. The request has the following format
        {
            "common_name":"2345"          # common name, which is the user id
        }
    """
    import pexpect

    vpn = TheVPNCompanyClient(url=VPN_API_URL, api_key=TVC_API_KEY)

    cn = str(request['common_name'])

    # generate the client
    log.info("Processing request: %s" % request)
    cmd = 'docker run  -i -v /var/thevpncompany/data/conf:/etc/openvpn --rm ' + VPN_OPENVPN_CONTAINER + ' ' \
          'easyrsa build-client-full "' + cn + '" nopass '
    log.debug("Executing: %s" % cmd)
    child = pexpect.spawn(cmd)
    child.expect('.*Enter pass phrase.*', timeout=5)
    log.debug("Sending password ...")
    child.sendline(CA_PASSWORD)
    log.debug("Password sent.")
    child.expect(pexpect.EOF)
    log.debug("Interaction finalized.")

    # prepare the certificates so are easy to read
    prepare_certs_response = execute('docker run -v /var/thevpncompany/data/conf:/etc/openvpn --rm kylemanna/openvpn '
                                     'ovpn_getclient ' + cn + ' separated')
    if prepare_certs_response.rc != 0:
        log.error("Error executing command, aborting certificate " + cn)
        return False

    client_private_file_response = execute("docker run -v /var/thevpncompany/data/conf:/etc/openvpn --rm "
                                           "kylemanna/openvpn cat /etc/openvpn/clients/" + cn + "/" + cn + ".key")

    client_public_file_response = execute("docker run -v /var/thevpncompany/data/conf:/etc/openvpn --rm "
                                          "kylemanna/openvpn cat /etc/openvpn/clients/" + cn + "/" + cn + ".crt")

    ca_file_response = execute("docker run -v /var/thevpncompany/data/conf:/etc/openvpn --rm "
                               "kylemanna/openvpn cat /etc/openvpn/clients/" + cn + "/ca.crt")

    static_key_file_response = execute("docker run -v /var/thevpncompany/data/conf:/etc/openvpn --rm "
                                       "kylemanna/openvpn cat /etc/openvpn/clients/" + cn + "/ta.key")

    save_cert_response = vpn.action_save_certificates(cn, client_private_file_response.stdout, client_public_file_response.stdout,
                                                      ca_file_response.stdout, static_key_file_response.stdout)

    if not save_cert_response:
        log.error("Can't save certificate")
        return False

    return True


if __name__ == "__main__":
    main()
