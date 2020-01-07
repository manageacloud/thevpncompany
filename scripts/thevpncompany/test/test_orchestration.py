#!/usr/bin/env python3

import unittest
from unittest.mock import patch
import json
from .mock import response
import logging

from ..consumer_orchestration import process_single_server

log = logging.getLogger(__name__)


def mocked_requests_post(*args, **kwargs):
    log.debug("Mock Post URL: %s" % args[0])

    if args[0] == 'https://manageacloud.com/api/v1/instance':
        return response.MockResponse(
            {"lifespan": 0, "ipv4": "", "servername": "1236-tor1", "id": "3kn1vlc0p3c403eeal9giqe2jd", "metadata": {
                "system": {"infrastructure": {"deployment": "production", "hardware": "s-1vcpu-1gb", "lifespan": -1,
                                              "location": "tor1", "provider": "digitalocean2"},
                           "role": {"cookbook_tag": "ovpn", "block_tags": [{}]}}}, "type": "production",
             "status": "Creating instance"}, 202)
    elif args[0] == 'http://localhost:8111/api/v1/server/1236/action':
        return response.MockResponse({"response": 0}, 200)

    return response.MockResponse(None, 404)


def mocked_requests_get(*args, **kwargs):
    log.debug("Mock Get URL: %s" % args[0])

    if args[0] == 'https://manageacloud.com/api/v1/instances':
        return response.MockResponse([{u'status': u'Ready', u'servername': u'test', u'lifespan': 0, u'ipv4': u'',
                                       u'type': u'production', u'id': u'3kn1vlc0p3c403eeal9giqe2jd', u'metadata': {
                u'system': {
                    u'infrastructure': {u'hardware': u'512mb', u'deployment': u'production', u'location': u'nyc1',
                                        u'lifespan': -1, u'provider': u'digitalocean2'},
                    u'role': {u'cookbook_tag': u'ovpn', u'block_tags': [{}]}}}}], 200)

    return response.MockResponse(None, 404)


class TestOrchestration(unittest.TestCase):

    @patch('requests.post', side_effect=mocked_requests_post)
    @patch('requests.get', side_effect=mocked_requests_get)
    def test_create_server(self, mock_post, mock_get):
        """
        Test the creation of a server
        """
        request = json.loads('{"server_id":1236, "supplier_id":1,"location_code":"tor1","user_id":3}')
        result = process_single_server(request)

        self.assertTrue(result)


if __name__ == '__main__':
    unittest.main()
