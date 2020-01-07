#!/usr/bin/env python3
"""
Server Creation Consumer

When a server is requested to be created, this script is invoked asynchronously.

This script manages the creation of a the server and notifies the web application
about the results.

The following environment variables customizes the behaviour of the scripts:
 - VPN_QUEUE_SERVER, ip that contains the beanstalkd daemon
 - VPN_QUEUE_PORT, port for the beanstalkd daemonm
 - VPN_QUEUE_TUBE, tube that contains the server requests
 - MANAGEACLOUD_AUTH, the credentials for ManageaCloud, the service that will create and configure the servers
 - VPN_API_URL, the URL to the HTTP(S) server that contains the VPN application

 The payload of a server request is a json script

"""

import greenstalk
import logging
import os
import json
from .components.macapi import ManageacloudClient
from .components.vpnapi import TheVPNCompanyClient

log = logging.getLogger(__name__)

VPN_QUEUE_SERVER = os.getenv('VPN_QUEUE_SERVER', "127.0.0.1")
log.debug("Queue server: %s" % VPN_QUEUE_SERVER)

VPN_QUEUE_PORT = os.getenv('VPN_QUEUE_PORT', 11300)
log.debug("Queue port: %s" % VPN_QUEUE_PORT)

VPN_QUEUE_TUBE = os.getenv('VPN_QUEUE_TUBE', "tube_orchestration")
log.debug("Queue tube: %s" % VPN_QUEUE_TUBE)

MANAGEACLOUD_AUTH = os.getenv('MANAGEACLOUD_AUTH')
log.debug("Manageacloud Auth: %s" % MANAGEACLOUD_AUTH)

MAC_CONFIGURATION_TAG = os.getenv('MAC_CONFIGURATION_TAG', "openvpn_28581")
log.debug("Manageacloud Configuration Tag: %s" % MAC_CONFIGURATION_TAG)

VPN_API_URL = os.getenv('VPN_API_URL', "http://localhost:8111")
log.debug("VPN API SERVER: %s" % VPN_API_URL)

TVC_API_KEY = os.getenv('TVC_API_KEY')
log.debug("The VPN Company API Key: %s" % TVC_API_KEY)

# repository that contains the configuration of the OpenVPN Server
CONFIGURATION_REPOSITORY = os.getenv('CONFIGURATION_REPOSITORY')
log.debug("Configuration Repository: %s" % CONFIGURATION_REPOSITORY)

# private key that is linked to the previous access keys of the repository
# and allow to clone and access its contents.
REPOSITORY_PRIVATE_KEY = os.getenv('REPOSITORY_PRIVATE_KEY')
log.debug("Private Key: %s" % REPOSITORY_PRIVATE_KEY)

ENV = os.getenv('ENV')
log.debug("Environment ENV: %s" % ENV)

if ENV is None or (ENV != "dev" and ENV != "prod" and ENV != "tests"):
    log.error("You need to configure the ENV environment variable. Possible values: 'dev' for development and 'prod' "
              "for production.")
    exit(1)

if CONFIGURATION_REPOSITORY is None or REPOSITORY_PRIVATE_KEY is None:
    log.error("You need to provide CONFIGURATION_REPOSITORY and REPOSITORY_PRIVATE_KEY. "
              "It allows to access to the configuration of OpenVPN.")
    exit(1)

if TVC_API_KEY is None:
    log.error("A password for the TheVPNCompany API (TVC_API_KEY) needs to be defined  ")
    exit(1)

# set logging level according to selected environment
if ENV == "prod" or ENV == "tests":
    logging.basicConfig(level=logging.WARN)
else:
    logging.basicConfig(level=logging.DEBUG)

if MANAGEACLOUD_AUTH is None:
    log.error("Unable to get the credentials for ManageaCloud. Please define MANAGEACLOUD_AUTH environment variable.")
    exit(1)


def main():
    """ Main process to loop on the queue """
    log.info("Starting Server Creation Consumer")

    try:
        with greenstalk.Client(host=VPN_QUEUE_SERVER, port=VPN_QUEUE_PORT, watch=VPN_QUEUE_TUBE,
                               use=VPN_QUEUE_TUBE) as queue:

            log.info("Daemon Ready")

            while True:
                log.debug("Waiting for job ... ")
                job = queue.reserve()
                if job is not None:
                    log.debug("[%s] New job received. Data: %s" % (job.id, job.body))
                    json_body = json.loads(job.body)
                    try:
                        result = process_single_server(json_body)
                        if result:
                            queue.delete(job)
                        else:
                            queue.bury(job)
                    except Exception as e:
                        log.error("A major error has occurred. Script cannot process job", exc_info=True)
                        queue.bury(job)
    except ConnectionRefusedError:
        log.error(
            "Unable to connect to ip %s:%s. Is beanstalkd the daemon running?" % (VPN_QUEUE_SERVER, VPN_QUEUE_PORT),
            exc_info=True)


def process_single_server(request) -> bool:
    """
        Process the creation of a single server. The request has the following format
        {
            "server_id":1236",          # application server id request
            "supplier_id":1,            # cloud supplier
            "location_code":"tor1",     # location's identifier in the cloud supplier
            "user_id":3                 # user who requested the creation of the server
        }
    """

    log.info("Processing request: %s" % request)
    mac = ManageacloudClient(auth=MANAGEACLOUD_AUTH, env=ENV)
    vpn = TheVPNCompanyClient(url=VPN_API_URL, api_key=TVC_API_KEY)

    if request['supplier_id'] == 1:  # digital ocean

        # notify TheVPNCompany that the server build just started
        result = vpn.action_server_requested(server_id=str(request['server_id']))

        if not result:
            log.error("server_id %s not correct ? We cannot mark the server as requested" % request['server_id'])
            return False

        # request new server
        mac_id = mac.create_server(hardware="s-1vcpu-1gb",
                                   cookbook_tag=MAC_CONFIGURATION_TAG,
                                   location=request['location_code'],
                                   servername=str(request['server_id']) + "-" + request['location_code'],
                                   provider="digitalocean2",
                                   repository_conf=CONFIGURATION_REPOSITORY,
                                   repository_private_key=REPOSITORY_PRIVATE_KEY)

        log.debug("Mac ID: %s" % mac_id)

        if not mac_id:  # creation request has been sent correct
            log.error("Could not create a server, mac id not available")
            vpn.action_server_failed(server_id=str(request['server_id']))
            return False

        # wait until server is configured and ready
        server = mac.wait(mac_id=mac_id)

        if server is None:  # creation request has been sent correct
            log.error("Could not configure the server, wait returned no server")
            vpn.action_server_failed(server_id=str(request['server_id']))
            return False

        log.debug("Server is ready and completed: %s " % server)
        vpn.action_server_completed(server_id=str(request['server_id']), id_external=server['id'],
                                    ipv4=server['ipv4'])

        return True


if __name__ == "__main__":
    main()
