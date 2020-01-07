#!/usr/bin/env python3
#
#  Discover users connected to a given openvpn
#
import logging
import json
import subprocess
import os

log = logging.getLogger(__name__)

ENV = os.getenv('ENV')
log.debug("Environment ENV: %s" % ENV)

MANAGEMENT_IP = os.getenv('MANAGEMENT_IP', '127.0.0.1')
log.debug("Environment MANAGEMENT_IP: %s" % MANAGEMENT_IP)

MANAGEMENT_PORT = os.getenv('MANAGEMENT_PORT', '7515')
log.debug("Environment MANAGEMENT_PORT: %s" % MANAGEMENT_PORT)

# set logging level according to selected environment
if ENV == "prod" or ENV == "tests":
    logging.basicConfig(level=logging.WARN)
else:
    logging.basicConfig(level=logging.DEBUG)


def find_openvpn_current_users():
    # get the clients that are currently connected
    clients_raw = subprocess.getoutput(
        'echo "status 3" | nc -w 1 ' + MANAGEMENT_IP + ' ' + MANAGEMENT_PORT + '| grep CLIENT_LIST | grep -v HEADER')
    log.debug("Raw Clients %s" % clients_raw)

    clients = clients_raw.split("\n")

    to_return = []
    for client in clients:
        line_clients = client.split("\t")
        if len(line_clients) > 1:
            log.debug("Client Data: %s" % line_clients)
            cn = line_clients[1]
            log.debug("Adding client %s" % cn)
            client = {
                "{#ID}": cn
            }
            to_return.append(client)

    return to_return


def main():
    log.debug("Starting OpenVPN User Discovery")
    zabbix_client_discovery = find_openvpn_current_users()
    print(json.dumps(zabbix_client_discovery))


if __name__ == '__main__':
    main()
