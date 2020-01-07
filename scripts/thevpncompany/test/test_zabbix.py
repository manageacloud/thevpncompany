#!/usr/bin/env python3

import unittest
from unittest.mock import patch, mock_open, MagicMock
import json
import logging
import os

from ..zabbix_openvpn_user_discovery import find_openvpn_current_users
from ..zabbix_openvpn_usage_per_user import find_openvpn_data_user

log = logging.getLogger(__name__)

MOCK_USER_DATA_USER1 = "CLIENT_LIST	1	49.195.124.134:64225	192.168.255.6		2984093	17687746	Thu Jul 18 03:31:11 2019	1563420671	UNDEF	10	0\n" \
                       "CLIENT_LIST	1	49.195.124.134:64226	192.168.255.10		41893	42003	Thu Jul 18 03:31:11 2019	1563420671	UNDEF	11	1\n"

MOCK_USER_DISCOVERY = "CLIENT_LIST	1	49.195.28.171:43884	192.168.255.6		8063733	22005266	Wed Jul 24 23:23:39 2019	1564010619	UNDEF	3	0\n" \
                      "CLIENT_LIST	2	49.195.28.171:43884	192.168.255.6		8063733	22005266	Wed Jul 24 23:23:39 2019	1564010619	UNDEF	3	1\n"


def mock_redis_get_none(*args, **kwargs):
    return None


def mock_redis_get(*args, **kwargs):
    return '{"timestamp": "2019-07-25 11:57:50", "total_session_sent_bytes": 17729749, "total_sent_bytes": 17729749}'.encode('utf-8')


class TestOpenVPNUserDiscovery(unittest.TestCase):

    @patch('subprocess.getoutput', return_value=MOCK_USER_DISCOVERY)
    def test_discover_openvpn_users(self, mock_post):
        """
        Test the zabbix user discovery
        """

        result = find_openvpn_current_users()

        self.assertEqual([{'{#ID}': '1'}, {'{#ID}': '2'}], result)


class TestOpenVPNUserData(unittest.TestCase):

    @patch('subprocess.getoutput', return_value=MOCK_USER_DATA_USER1)
    @patch('redis.Redis.get', side_effect=mock_redis_get_none)
    def test_get_user_data(self, mock_post, mock_redis_get):
        """
        Fetch user bandwidth usage
        """
        with patch('configparser.ConfigParser.read', MagicMock(return_value={})):
            result = find_openvpn_data_user("1")

        self.assertEqual(17729749, result)

    @patch('subprocess.getoutput', return_value=MOCK_USER_DATA_USER1)
    @patch('redis.Redis.get', side_effect=mock_redis_get)
    def test_get_user_data_no_new_traffic(self, mock_post, mock_redis_get):
        """
        Fetch user bandwidth usage
        """
        result = find_openvpn_data_user("1")

        self.assertEqual(0, result)


if __name__ == '__main__':
    unittest.main()
