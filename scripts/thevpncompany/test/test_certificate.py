#!/usr/bin/env python3

import unittest
from unittest.mock import patch
import json
from .mock import response
import logging

from ..consumer_openvpn_certificates import process_single_certificate

log = logging.getLogger(__name__)


def mocked_vpnapi_save_certificate(*args, **kwargs):
    return response.MockResponse({"response": 0}, 200)


class TestCertificates(unittest.TestCase):

    # TODO: mock docker requests
    @patch('requests.put',   side_effect=mocked_vpnapi_save_certificate)
    def test_create_server(self, mock_post):
        """
        Test the creation of a server
        """
        import random
        request = json.loads('{"common_name": "random@email.com' + str(random.randint(1, 10000)) + '"}')
        result = process_single_certificate(request)

        self.assertTrue(result)


if __name__ == '__main__':
    unittest.main()
