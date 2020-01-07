package com.manageacloud.vpn.service;

import com.manageacloud.vpn.config.VpnConfigurationTests;
import com.manageacloud.vpn.model.api.v1.ApiV1ServerActionDTO;
import com.manageacloud.vpn.model.cloud.Server;
import com.manageacloud.vpn.repository.cloud.ServerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
@SpringBootTest(classes = VpnConfigurationTests.class)
@TestPropertySource(locations="classpath:vpn-server-test.yml")
public class CloudServiceTest {

    @Autowired
    CloudService cloudService;

    @Autowired
    ServerRepository serverRepository;

    @Test
    public void markServerAsRequestedTest() throws Exception {
        Long serverId = 1L;
        java.util.Optional<Server> optionalServer = serverRepository.findById(serverId);
        Server server = optionalServer.orElseThrow();

        assertNotNull(server);
        assertNull(server.getCreationRequest());
        assertNull(server.getCreationCompleted());
        assertNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionFailed());

        ApiV1ServerActionDTO action = new ApiV1ServerActionDTO("CreationRequest");
        cloudService.processAction(serverId, action);

        server = serverRepository.findById(serverId).orElseThrow();
        assertNotNull(server.getCreationRequest());
        assertNull(server.getCreationCompleted());
        assertNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionFailed());

    }

    @Test
    public void markServerAsRequestFailedTest() throws Exception {
        Long serverId = 2L;
        java.util.Optional<Server> optionalServer = serverRepository.findById(serverId);
        Server server = optionalServer.orElseThrow();

        assertNotNull(server);
        assertNotNull(server.getCreationRequest());
        assertNull(server.getCreationCompleted());
        assertNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionFailed());

        ApiV1ServerActionDTO action = new ApiV1ServerActionDTO("CreationFailed");
        cloudService.processAction(serverId, action);

        server = serverRepository.findById(serverId).orElseThrow();
        assertNotNull(server.getCreationRequest());
        assertNull(server.getCreationCompleted());
        assertNotNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionFailed());

    }

    @Test
    public void markServerAsRequestCompletedTest() throws Exception {
        Long serverId = 3L;
        java.util.Optional<Server> optionalServer = serverRepository.findById(serverId);
        Server server = optionalServer.orElseThrow();

        assertNull(server.getId_external());
        assertNull(server.getIpv4());

        assertNotNull(server);
        assertNotNull(server.getCreationRequest());
        assertNull(server.getCreationCompleted());
        assertNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionFailed());

        ApiV1ServerActionDTO action = new ApiV1ServerActionDTO("CreationReady", "1.2.3.4", "my_id_external");
        cloudService.processAction(serverId, action);

        server = serverRepository.findById(serverId).orElseThrow();

        assertEquals("my_id_external", server.getId_external());
        assertEquals("1.2.3.4", server.getIpv4());

        assertNotNull(server.getCreationRequest());
        assertNotNull(server.getCreationCompleted());
        assertNull(server.getCreationFailed());
        assertNull(server.getDeletionRequest());
        assertNull(server.getDeletionFailed());

    }

}