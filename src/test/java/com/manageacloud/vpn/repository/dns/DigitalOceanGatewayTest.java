/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.dns;

import com.manageacloud.vpn.config.VpnConfigurationTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VpnConfigurationTests.class)
public class DigitalOceanGatewayTest {

    @Autowired
    DigitalOceanGateway digitalOceanGateway;

    @Test
    public void createCNAME() {
        //digitalOceanRepository.createCNAME(null, "test");
    }

    @Test
    public void updateCNAME() {
        //digitalOceanRepository.updateCNAME(null, "test");
    }

    @Test
    public void deleteCNAME() {

        //digitalOceanRepository.deleteCNAME(null, "test");
    }

}