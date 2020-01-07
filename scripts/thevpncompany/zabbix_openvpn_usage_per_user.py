#!/usr/bin/env python3
#
#  Returns the number of bytes used since the last time
#  this script has been called
#
#
import logging
import json
import subprocess
import os
import sys
import json
from datetime import datetime, timedelta
import calendar

import redis as redis_client

log = logging.getLogger(__name__)

ENV = os.getenv('ENV')
log.debug("Environment ENV: %s" % ENV)

MANAGEMENT_IP = os.getenv('MANAGEMENT_IP', '127.0.0.1')
log.debug("Environment MANAGEMENT_IP: %s" % MANAGEMENT_IP)

MANAGEMENT_PORT = os.getenv('MANAGEMENT_PORT', '7515')
log.debug("Environment MANAGEMENT_PORT: %s" % MANAGEMENT_PORT)

# log file
LOG_FILE = "/var/log/zabbix/openvpn_user_traffic.log"

# set logging level according to selected environment
# if ENV == "prod" or ENV == "tests":
#    logging.basicConfig(level=logging.WARN)
# else:
#    logging.basicConfig(level=logging.DEBUG)
# enable logging into a file
logging.basicConfig(level=logging.DEBUG, filename=LOG_FILE, filemode='a',
                    format='%(name)s - %(levelname)s - %(message)s')


def find_openvpn_data_user(user):
    """
    Calculate the amount of received / send bytes per user, for all devices

    We are just interested on returning the amount of bytes sent from the openvpn server to the user.

    :param user: name of the user used in the openvpn certificate used to authenticate
    :return:
    """

    log.debug("Requesting data for user: %s" % user)

    clients_data_raw = subprocess.getoutput(
        'echo "status 3" | nc -w 1 ' + MANAGEMENT_IP + ' ' + MANAGEMENT_PORT + '| grep CLIENT_LIST | grep -v HEADER | grep ' + user)
    log.debug("Raw client data %s" % clients_data_raw)

    data_lines = clients_data_raw.split("\n")

    # prepare the config file with the data
    # config = configparser.ConfigParser()

    # try:
    #     config.read(CFG_FILE)
    # except Exception:
    #     log.debug("Can't open %s: " % Exception)

    redis = redis_client.Redis(host='localhost', port=6379, db=0)

    redis_key = 'client_' + user

    previous_total_session_sent_bytes = 0
    previous_total_sent_bytes = 0
    sent_bytes = 0

    for data_line in data_lines:
        line = data_line.split("\t")
        log.debug("Processing line: %s" % line)
        if len(line) > 11:
            line_user = line[1]
            log.debug("User CN: %s" % line_user)
            if line_user == user:  # sanity check

                # collect all the send data for all pees
                peer_id = line[11]
                sent_bytes += int(line[6])

                log.debug("Peer %s found, send_bytes %s" % (peer_id, line[6]))

    previous_value_raw = redis.get(redis_key)

    if previous_value_raw is None:
        log.info("Previous values not found. Starting with fresh data.")
    else:
        log.debug("Previous values found. Raw data: %s" % previous_value_raw)
        # we can have different peers with the same user
        previous_value = json.loads(previous_value_raw.decode('utf-8'))
        previous_total_session_sent_bytes = previous_value['total_session_sent_bytes']
        previous_total_sent_bytes = previous_value['total_sent_bytes']
        previous_timestamp = previous_value['timestamp']
        log.info("Previous data available: previous_total_session_sent_bytes %s, previous_total_sent_bytes: %s" % (
            previous_total_session_sent_bytes, previous_total_sent_bytes))

    if sent_bytes < previous_total_session_sent_bytes:  # the session is new
        session_sent_bytes = sent_bytes
    else:  # the session already existed
        session_sent_bytes = sent_bytes - previous_total_session_sent_bytes

    # total number of bytes sent in the current openvpn session for all peers
    total_session_sent_bytes = previous_total_session_sent_bytes + session_sent_bytes

    # total number of bytes sent this month in this server
    total_sent_bytes = previous_total_sent_bytes + session_sent_bytes

    if previous_value_raw is None:
        # we are creating the key, so we need to put the expiration time
        now = datetime.now()
        days_in_month = calendar.monthrange(now.year, now.month)[1]
        log.debug("Setting expiration date to %s days " % days_in_month)
        expiration_date = timedelta(days=days_in_month)

        config_data = {
            'total_session_sent_bytes': session_sent_bytes,
            'total_sent_bytes': 0,
            'timestamp': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        }

        redis.set(redis_key, json.dumps(config_data))
        redis.expire(redis_key, expiration_date)

    else:
        log.debug("Updating sent_bytes ")

        config_data = {
            'total_session_sent_bytes': total_session_sent_bytes,
            'total_sent_bytes': total_sent_bytes,
            'timestamp': previous_timestamp,
        }
        redis.set(redis_key, json.dumps(config_data))

    log.debug("Final data: %s" % config_data)
    log.debug("Request sent bytes: %s" % session_sent_bytes)

    return total_sent_bytes


def main():
    log.debug("Starting OpenVPN User Data Used")

    # read the username
    user = sys.argv[1]
    if user is None:
        log.error("A parameter with the username is required.")
        exit(1)

    zabbix_client_data = find_openvpn_data_user(user)

    print(json.dumps(zabbix_client_data))


if __name__ == '__main__':
    main()
