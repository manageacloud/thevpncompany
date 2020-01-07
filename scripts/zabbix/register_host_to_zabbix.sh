#!/bin/bash
set -x

usage () {
    cat <<HELP_USAGE

	Usage: `basename $0` zabbix_server api_username api_password template_id_linux_base template_id_vpn template_id_openvpn

	  This script must be executed from the server that is going to be registered in Zabbix.

	  Parameters:
	   - zabbix_server:  the full URL to the zabbix server, including the protocol and any basic additional auth (if required). Example: https://optional_basic_auth_user:optional_basic_auth_password@zabbix.myserver.com
           - api_username: a Zabbix user that has access to the templates and with permissions to create a new host server
           - api_password: password for api_username 
           - template_id_linux_base: template id base for the server
           - template_id_vpn: template id for the vpn
           - template_id_openvpn: template id for the vpn template.
HELP_USAGE
}

echo "Params: $#"

# if no parameters, show the help.
if [ $# -lt 6 ]
  then
    usage
    exit
fi

ZABBIX_SERVER=$1
API_USERNAME=$2
API_PASSWORD=$3
TEMPLATE_BASE=$4
TEMPLATE_VPN=$5
TEMPLATE_OPENVPN=$6

# get the server IP
ip=$(curl -s 'https://api.ipify.org?format=text')

# login
cat > /tmp/login.json << EOL
{
    "jsonrpc": "2.0",
    "method": "user.login",
    "params": {
        "user": "${API_USERNAME}",
        "password": "${API_PASSWORD}"
    },
    "id": 1
}
EOL
auth="$(curl -sX POST ${ZABBIX_SERVER}/zabbix/api_jsonrpc.php -d @/tmp/login.json -H 'Content-Type: application/json' | sed -n 's/.*"result":"\(.*\)",".*/\1/p')"


# create host
cat > /tmp/create.host.json << EOL
{
    "jsonrpc": "2.0",
    "method": "host.create",
    "params": {
        "host": "`hostname`",
        "interfaces": [
            {
                "type": 1,
                "main": 1,
                "useip": 1,
                "ip": "$ip",
                "dns": "",
                "port": "10050"
            }
        ],
        "groups": [
            {
                "groupid": "15"
            }
        ],
        "templates": [
            {
                "templateid": "${TEMPLATE_BASE}"
            },
            {
                "templateid": "${TEMPLATE_VPN}"
            },
            {
                "templateid": "${TEMPLATE_OPENVPN}"
            }
        ]
    },
    "auth": "$auth",
    "id": 1
}
EOL
curl -sX POST ${ZABBIX_SERVER}/zabbix/api_jsonrpc.php -d @/tmp/create.host.json -H 'Content-Type: application/json'

# log out
cat > /tmp/logout.json << EOL
{
    "jsonrpc": "2.0",
    "method": "user.logout",
    "params": [],
    "id": 1,
    "auth": "$auth"
}
EOL
curl -sX POST ${ZABBIX_SERVER}/zabbix/api_jsonrpc.php -d @/tmp/logout.json -H 'Content-Type: application/json' > /dev/null


# clean up
## rm /tmp/logout.json /tmp/create.host.json /tmp/login.json

