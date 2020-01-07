
#
#  Open VPN Server Name
#
OPENVPN_SERVER_NAME := ${OPENVPN_SERVER_NAME}

# creates the configuration files and the private keys
new-server-configuration:
ifndef OPENVPN_SERVER_NAME
	$(error OPENVPN_SERVER_NAME is undefined)
endif
	#[ -f /tmp/etc_openvpn ] && echo "The directory /tmp/etc_openvpn already exists. Please delete it to re-create the keys".
	docker run  -i -v /tmp/etc_openvpn:/etc/openvpn --rm kylemanna/openvpn:2.4 ovpn_genconfig \
    		-u udp://${OPENVPN_SERVER_NAME} \
    		-k "5 10" \
    		-e "cipher AES-256-CBC" \
    		-e "management localhost 7505" \
    		-z
	docker run  -i -v /tmp/etc_openvpn:/etc/openvpn --rm kylemanna/openvpn:2.4 ovpn_initpki

# installs docker compose
install-docker-compose:
	curl -s -L "https://github.com/docker/compose/releases/download/1.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
	chmod +x /usr/local/bin/docker-compose

# run the Open VPN Serverr
run:
	/usr/local/bin/docker-compose up
