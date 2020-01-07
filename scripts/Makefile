#
# Environment variables required on this script:
#  - ENV can be 'prod' for production, 'dev' for development, and 'tests' when running tests.
#  - MANAGEACLOUD_AUTH is the authentication required for the API
#	 of Manageacloud (https://manageacloud.com). It is a pair such as 'username:key'
#    Documenation: https://manageacloud.com/article/orchestration/api/authentication
#  - CA_PASSWORD is the password used by the OpenVPN private certificate
#

freeze:
	. env/bin/activate ;\
	pip freeze --local > requirements.txt

build:
	virtualenv env -p /usr/bin/python3; \
	env/bin/pip install -r requirements.txt

clean:
	rm -rf env build

rebuild: clean build

tests: rebuild
	MANAGEACLOUD_AUTH=dummy:password ENV=tests /usr/bin/python3 -m thevpncompany.test.test_orchestration
	#ENV=tests python -m thevpncompany.test.test_certificate
	ENV=tests /usr/bin/python3 -m thevpncompany.test.test_zabbix

install:
	python3 setup.py install
	# clean all temporal folders
	python3 setup.py clean --all
	rm -rf build
	rm -rf thevpncompany.egg-info

publish-pypi:
	python setup.py sdist
	twine upload dist/*

orchestration_stats:
	echo "stats-tube tube_orchestration" | nc -C localhost 11300

orchestration-run: check-orchestration-env
	python3 -m thevpncompany.consumer_orchestration

openvpn-create-cert-run: check-openvpn-create-cert-env
	python3 -m thevpncompany.consumer_openvpn_certificates

check-orchestration-env:
ifndef ENV
	$(error ENV is undefined)
endif
ifndef MANAGEACLOUD_AUTH
	$(error MANAGEACLOUD_AUTH is undefined)
endif
ifndef TVC_API_KEY
	$(error TVC_API_KEY is undefined)
endif
ifndef CONFIGURATION_REPOSITORY
	$(error CONFIGURATION_REPOSITORY is undefined)
endif
ifndef REPOSITORY_PRIVATE_KEY
	$(error REPOSITORY_PRIVATE_KEY is undefined)
endif


check-openvpn-create-cert-env:
ifndef ENV
	$(error ENV is undefined)
endif
ifndef CA_PASSWORD
	$(error CA_PASSWORD is undefined)
endif
ifndef TVC_API_KEY
	$(error TVC_API_KEY is undefined)
endif
