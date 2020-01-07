TheVPNCompany
=============

`TheVPNCompany <https://thevpncompany.com.au>`_ is an Open Source VPN company who intends to:
 - Increase transparency, allowing to be audited by anyone.
 - Increase the usage of VPNs around, based on companies and practices that are transparent.

This package consists on a set of daemons and scripts required for different subsystems.

1 - `consumer_orchestration.py`, consumer that creates fully configured OpenVPN servers in 120 seconds.

2 - `consumer_certificates.py`, consumer that creates new OpenVPN certificates.

The following scripts provides information to Zabbix monitoring systems:

1 - `zabbix_openvpn_user_discovery.py`, which discover connected OpenVPN Users using the management console

2 - `zabbix_openvpn_usage_per_user.py`, which reports the data usage per user

3 - `zabbix_track_monthly_traffic.py`, which reports the OpenVPN data usage per month


Installation
============

Install the scripts from the source code

.. code:: bash

    python setup.py install

Install the scripts from `pip`

.. code:: bash

    pip install thevpncompany



Documentation
=============

You can find the documentation for all the released Open Source code on TheVPNCompany website: https://thevpncompany.com.au

