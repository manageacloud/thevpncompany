/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.controller;

import com.manageacloud.vpn.config.VpnConfigurationTests;
import com.manageacloud.vpn.mock.user.WithMockUserJohnSmith;
import com.manageacloud.vpn.model.Location;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.UserLocation;
import com.manageacloud.vpn.model.cloud.Server;
import com.manageacloud.vpn.repository.LocationRepository;
import com.manageacloud.vpn.repository.UserLocationRepository;
import com.manageacloud.vpn.repository.UserRepository;
import com.manageacloud.vpn.repository.cloud.ServerRepository;
import com.manageacloud.vpn.repository.dns.DigitalOceanGateway;
import com.myjeeva.digitalocean.pojo.DomainRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = VpnConfigurationTests.class)
@TestPropertySource(locations="classpath:vpn-server-test.yml")
@PropertySource("classpath:secrets-test.properties")
public class DashboardControllerTest {

    private static final Logger log = LoggerFactory.getLogger(DashboardControllerTest.class);

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    UserLocationRepository userLocationRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DigitalOceanGateway digitalOceanGateway;

    @Autowired
    ServerRepository serverRepository;

    @Autowired
    private MockMvc mockMvc;

    private CsrfToken csrfToken;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac)
                .build();
        HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        this.csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

        // clean up all CNAME tests
        digitalOceanGateway.cleanUpAllTests();

    }

    @Test
    public void configurationDownload() throws Exception {
    }


    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void updateLocationExitingLocationTest() throws Exception {

        // create subscription
        String userEmail = "john.smith@unittest.com";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("email", userEmail)
                .param("plan", "3")
                .param("cc1", "5520")
                .param("cc2", "0000")
                .param("cc3", "0000")
                .param("cc4", "0000")
                .param("month", "05")
                .param("year", "25")
                .param("cvv", "000")

                .param("name", "John Smith")
                .param("address", "unit 2 / something street ")
                .param("city", "Randwick")
                .param("country", "Australia")

        )
                .andExpect(status().is2xxSuccessful());

        User user = userRepository.findOneByEmail(userEmail);

        UserLocation userLocation = userLocationRepository.findByUser(user).orElseThrow();
        Location defaultLocation = locationRepository.findDefaultLocation();
        DomainRecord domainRecord = digitalOceanGateway.findCNAME(userLocation);
        assertTrue(userLocation.getUserHostname().startsWith(domainRecord.getName()));
        assertEquals(defaultLocation.getHostname(), domainRecord.getData());

        assertEquals(userLocation.getLocation().getIso(), defaultLocation.getIso());

        // update to location that has already a server
        Location newLocation = locationRepository.findLocationByIso(Location.Type.US_CA.toString());
        mockMvc.perform(post("/dashboard/location/xhr").with(csrf())
                .param("iso", newLocation.getIso()))
                .andExpect(status().is2xxSuccessful());

        UserLocation newUserLocation = userLocationRepository.findByUser(user).orElseThrow();

        assertEquals(newUserLocation.getLocation().getIso(), newLocation.getIso());

        DomainRecord newDomainRecord = digitalOceanGateway.findCNAME(newUserLocation);
        assertTrue(newUserLocation.getUserHostname().startsWith(newDomainRecord.getName()));
        assertEquals(newLocation.getHostname(), newDomainRecord.getData());

    }


    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void updateLocationCreatesNewServerTest() throws Exception {

        // create subscription
        String userEmail = "john.smith@unittest.com";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("email", userEmail)
                .param("plan", "3")
                .param("cc1", "5520")
                .param("cc2", "0000")
                .param("cc3", "0000")
                .param("cc4", "0000")
                .param("month", "05")
                .param("year", "25")
                .param("cvv", "000")

                .param("name", "John Smith")
                .param("address", "unit 2 / something street ")
                .param("city", "Randwick")
                .param("country", "Australia")

        )
                .andExpect(status().is2xxSuccessful());

        User user = userRepository.findOneByEmail(userEmail);

        UserLocation defaultUserLocation = userLocationRepository.findByUser(user).orElseThrow();
        Location defaultLocation = locationRepository.findDefaultLocation();
        DomainRecord domainCNAMERecord = digitalOceanGateway.findCNAME(defaultUserLocation);
        assertTrue(defaultUserLocation.getUserHostname().startsWith(domainCNAMERecord.getName()));
        assertEquals(defaultLocation.getHostname(), domainCNAMERecord.getData());

        assertEquals(defaultUserLocation.getLocation().getIso(), defaultLocation.getIso());

        // update to existing location that does not have servers
        Location newLocation = locationRepository.findLocationByIso(Location.Type.CA_ON.toString());
        mockMvc.perform(post("/dashboard/location/xhr").with(csrf())
                .param("iso", newLocation.getIso()))
                .andExpect(status().is2xxSuccessful());

        // for this case, the location should not be updated yet, as the server is being created.
        UserLocation newUserLocation = userLocationRepository.findByUser(user).orElseThrow();
        assertEquals(defaultLocation.getIso(), newUserLocation.getLocation().getIso());
        domainCNAMERecord = digitalOceanGateway.findCNAME(defaultUserLocation);
        assertTrue(newUserLocation.getUserHostname().startsWith(domainCNAMERecord.getName()));
        assertEquals(defaultLocation.getHostname(), domainCNAMERecord.getData());

        // there is another process who creates the server. So we will emulate the API calls
        // required if the server is created successfully.
        Server server = serverRepository.findFirstByOrderByCreatedDesc();
        assertNull(server.getCreationRequest());
        assertNull(server.getCreationCompleted());
        assertNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionCompleted());
        assertNull(server.getDeletionFailed());


        // send creation request, server has been requested
        mockMvc.perform(post("/api/v1/server/" + server.getId() + "/action")
                .header("apiKey", "1234")
                .param("action", "CreationRequest"))
                .andExpect(status().is2xxSuccessful());

        server = serverRepository.findById(server.getId()).orElseThrow();
        assertNotNull(server.getCreationRequest());
        assertNull(server.getCreationCompleted());
        assertNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionCompleted());
        assertNull(server.getDeletionFailed());

        // send creation successfully completed, server has been configured and it is ready to operate
        mockMvc.perform(post("/api/v1/server/" + server.getId() + "/action")
                .header("apiKey", "1234")
                .param("action", "CreationReady")
                .param("id_external", "my_id_external")
                .param("ipv4", "1.2.3.4"))
                .andExpect(status().is2xxSuccessful());

        server = serverRepository.findById(server.getId()).orElseThrow();
        assertNotNull(server.getCreationRequest());
        assertNotNull(server.getCreationCompleted());
        assertNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionCompleted());
        assertNull(server.getDeletionFailed());

        assertEquals("my_id_external", server.getId_external());
        assertEquals("1.2.3.4", server.getIpv4());

        // Check that the user has been configured to use the new location
        newUserLocation = userLocationRepository.findByUser(user).orElseThrow();
        assertEquals(newLocation.getIso(), newUserLocation.getLocation().getIso());
        domainCNAMERecord = digitalOceanGateway.findCNAME(defaultUserLocation);
        assertTrue(newUserLocation.getUserHostname().startsWith(domainCNAMERecord.getName()));
        assertEquals(newLocation.getHostname(), domainCNAMERecord.getData());

        // check that the new location is using this new server
        DomainRecord domainARecord = digitalOceanGateway.find(newLocation);
        assertEquals("1.2.3.4", domainARecord.getData());

    }


    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void updateLocationRequestNewServerTwiceTest() throws Exception {

        int initialServersNum = serverRepository.findAll().size();

        // create subscription
        String userEmail = "john.smith@unittest.com";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("email", userEmail)
                .param("plan", "3")
                .param("cc1", "5520")
                .param("cc2", "0000")
                .param("cc3", "0000")
                .param("cc4", "0000")
                .param("month", "05")
                .param("year", "25")
                .param("cvv", "000")

                .param("name", "John Smith")
                .param("address", "unit 2 / something street ")
                .param("city", "Randwick")
                .param("country", "Australia")

        )
                .andExpect(status().is2xxSuccessful());

        User user = userRepository.findOneByEmail(userEmail);

        List<Server> servers = serverRepository.findAll();
        assertEquals(initialServersNum, servers.size());

        // update location
        Location newLocation = locationRepository.findLocationByIso(Location.Type.CA_ON.toString());
        mockMvc.perform(post("/dashboard/location/xhr").with(csrf())
                .param("iso", newLocation.getIso()))
                .andExpect(status().is2xxSuccessful());

        UserLocation newUserLocation = userLocationRepository.findByUser(user).orElseThrow();

        // as the server is being built, the user should not be configured to the new location just yet.
        assertNotEquals(newUserLocation.getLocation().getIso(), newLocation.getIso());
        DomainRecord newDomainRecord = digitalOceanGateway.findCNAME(newUserLocation);
        assertTrue(newUserLocation.getUserHostname().startsWith(newDomainRecord.getName()));
        assertNotEquals(newLocation.getHostname(), newDomainRecord.getData());

        // check that the server has been requested
        servers = serverRepository.findAll();
        assertEquals(initialServersNum + 1, servers.size());

        // update location
        mockMvc.perform(post("/dashboard/location/xhr").with(csrf())
                .param("iso", newLocation.getIso()))
                .andExpect(status().is2xxSuccessful());

        // check that the server has been requested
        // as the first server requested has not been completed, the second request should
        // be ignored
        servers = serverRepository.findAll();
        assertEquals(initialServersNum + 1, servers.size());

    }



}