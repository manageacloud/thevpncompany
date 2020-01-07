#!/usr/bin/env python3
"""
Checks if the user is valid to use the VPN according to the central TheVPNCompany database.

The following environment variables customizes the behaviour of the scripts:
 - VPN_API_URL, the URL to the HTTP(S) server that contains the VPN application

 OpenVPN executes this script sending the information about the certificate and the Common Name (cn)
 that has connected. This common name is the user_id of TheVPNCompany, so an API call
 can confirm if the user has permission to access to the VPN services.

"""

import logging
import os
import sys
import re
from .components.vpnapi import TheVPNCompanyClient

__author__ = "Ruben Rubio Rey"
__version__ = "0.1.0"
__license__ = "MIT"

log = logging.getLogger(__name__)

VPN_API_URL = os.getenv('VPN_API_URL', "https://thevpncompany.com.au")
log.debug("VPN API SERVER: %s" % VPN_API_URL)

ENV = os.getenv('ENV', "dev")
log.debug("Environment ENV: %s" % ENV)

if ENV is None or (ENV != "dev" and ENV != "prod" and ENV != "tests"):
    log.error("You need to configure the ENV environment variable. Possible values: 'dev' for development and 'prod' "
              "for production.")
    exit(1)

# set logging level according to selected environment
if ENV == "prod" or ENV == "tests":
    logging.basicConfig(level=logging.WARN)
else:
    # logging.basicConfig(level=logging.DEBUG)
    logging.basicConfig(level=logging.DEBUG, filename="/tmp/vpn_is_user_valid.log", filemode='a',
                        format='%(name)s - %(levelname)s - %(message)s')


def main():
    """
    depth        -- The current certificate chain depth.  In a typical
                     bi-level chain, the root certificate will be at level
                     1 and the client certificate will be at level 0.
                     This script will be called separately for each level.
    x509         -- the X509 subject string as extracted by OpenVPN from
                    the client's provided certificate.

    Credits:  https://robert.penz.name/21/ovpncncheck-an-openvpn-tls-verify-script/
    """

    # log.debug("Environments")
    # log.debug(os.environ)

    log.info("Starting Validation of User")

    (depth, x509) = sys.argv[1:]

    log.debug("Input dept: %s" % depth)

    log.debug("Input x509: %s" % x509)

    if int(depth) == 0:
        # If depth is zero, we know that this is the final
        # certificate in the chain (i.e. the client certificate),
        # and the one we are interested in examining.
        # If so, parse out the common name substring in
        # the X509 subject string.
        log.debug("Depth is zero: %s" % depth)
        found = re.compile(r"CN=([0-9]+)").search(x509)
        log.debug("Found: %s")
        log.debug(found)
        if found:
            # Client certificate common  name
            cn = found.group(1)
            log.debug("Client common name found: " + cn)

            # check if the user is valid
            if check_user(cn):
                sys.exit(0)

        else:
            log.debug("CN Not found:")

        # Authentication failed -- Either we could not parse
        # the X509 subject string, or the common name in the
        # subject string didn't match the user list regexes,
        # or main database system has rejected the user as valid
        sys.exit(1)

    # If depth is nonzero, tell OpenVPN to continue processing
    # the certificate chain.
    sys.exit(0)


def check_user(user_id) -> bool:
    """
        Process the creation of a single server. The request has the following format
        {
            "server_id":1236",          # application server id request
            "supplier_id":1,            # cloud supplier
            "location_code":"tor1",     # location's identifier in the cloud supplier
            "user_id":3                 # user who requested the creation of the server
        }
    """

    log.info("Processing request: %s" % user_id)
    vpn = TheVPNCompanyClient(url=VPN_API_URL, api_key="") # API Key not needed for this request

    # notify TheVPNCompany that the server build just started
    return vpn.is_user_valid(user_id=str(user_id))


if __name__ == "__main__":
    main()
