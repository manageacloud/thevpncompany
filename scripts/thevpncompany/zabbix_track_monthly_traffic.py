#!/usr/bin/env python3
#
#  Tracks monthly bandwidth usage
#

import calendar
import datetime
import logging
import os
from dateutil import parser

import configparser

log = logging.getLogger(__name__)

# network that we are monitoring
IF = "wlp1s0"

# configuration file
CFG_FILE = "/etc/zabbix/zabbix_agentd.d/traffic.ini"

ENV = os.getenv('ENV', "prod")
log.debug("Environment ENV: %s" % ENV)

# set logging level according to selected environment
if ENV == "prod" or ENV == "tests":
    logging.basicConfig(level=logging.WARN)
else:
    logging.basicConfig(level=logging.DEBUG)


def main():
    log.debug("Starting Monthly Traffic Monitoring")

    # bytes received before this month
    offset_rx_bytes = None

    # bytes sent before this month
    offset_tx_bytes = None

    # date range for this data
    date_from = None
    date_to = None

    try:
        # read the configuration file
        config = configparser.ConfigParser()
        log.debug("Reading file %s section %s" % (CFG_FILE, IF))
        config.read(CFG_FILE)
        date_from = parser.parse(config.get(IF, 'date_from'))
        date_to = parser.parse(config.get(IF, 'date_to'))
        offset_rx_bytes = int(config.get(IF, 'offset_rx_bytes'))
        offset_tx_bytes = int(config.get(IF, 'offset_tx_bytes'))
    except:
        log.debug("Configuration file produced an error, therefore we will create a new one")
        date_from, date_to, offset_rx_bytes, offset_tx_bytes = save_current_config()

    if datetime.date(date_to.year, date_to.month, date_to.day) < datetime.date.today():
        # data has expired, we are in a new month
        log.debug('Today %s is older than %s, regenerating config file' % (str(datetime.date.today()), str(date_to)))
        date_from, date_to, offset_rx_bytes, offset_tx_bytes = save_current_config()

    log.debug('Date range: %s to %s' % (str(date_from), str(date_to)))
    log.debug('%s offset bytes received' % offset_rx_bytes)
    log.debug('%s offset bytes sent' % offset_tx_bytes)

    current_rx_bytes, current_tx_bytes = get_network_bytes(IF)
    log.debug('%s current bytes received' % current_rx_bytes)
    log.debug('%s current bytes sent' % current_tx_bytes)

    rx_bytes = current_rx_bytes - offset_rx_bytes
    tx_bytes = current_tx_bytes - offset_tx_bytes
    log.debug('%s date range bytes received' % rx_bytes)
    log.debug('%s date range bytes sent' % tx_bytes)

    # transform data to zabbix client, transmitted dat
    print(tx_bytes)


# get network bytes for the interface
def get_network_bytes(interface):
    for line in open('/proc/net/dev', 'r'):
        if interface in line:
            data = line.split('%s:' % interface)[1].split()
            rx_bytes, tx_bytes = (data[0], data[8])
            return int(rx_bytes), int(tx_bytes)


# add months to a date
def add_months(sourcedate, months):
    month = sourcedate.month - 1 + months
    year = sourcedate.year + month // 12
    month = month % 12 + 1
    day = min(sourcedate.day, calendar.monthrange(year, month)[1])
    return datetime.date(year, month, day)


# save config
def save_current_config():
    config = configparser.ConfigParser()
    offset_rx_bytes, offset_tx_bytes = get_network_bytes(IF)
    date_from = datetime.date.today()
    date_to = add_months(date_from, 1)

    log.debug("Creating file %s section %s" % (CFG_FILE, IF))

    config[IF] = {
        'date_from': date_from,
        'date_to': date_to,
        'offset_rx_bytes': offset_rx_bytes,
        'offset_tx_bytes': offset_tx_bytes
    }

    with open(CFG_FILE, 'w') as configfile:
        config.write(configfile)
        configfile.close()

    return date_from, date_to, offset_rx_bytes, offset_tx_bytes


if __name__ == '__main__':
    main()
