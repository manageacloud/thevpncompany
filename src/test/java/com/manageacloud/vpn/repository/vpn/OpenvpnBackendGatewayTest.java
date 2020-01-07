/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.vpn;

import com.manageacloud.vpn.config.VpnConfigurationTests;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
@SpringBootTest(classes = VpnConfigurationTests.class)
@TestPropertySource(locations="classpath:vpn-server-test.yml")
public class OpenvpnBackendGatewayTest {

    private static final Logger log = LoggerFactory.getLogger(OpenvpnBackendGateway.class);

    @Autowired
    OpenvpnBackendGateway openvpnBackendGateway;

    @Autowired
    UserRepository userRepository;

    @Test
    public void generateClientTest() throws Exception {

        User user = userRepository.findOneByEmail("test2@email.com");

        openvpnBackendGateway.generateClient(user);

    }


}