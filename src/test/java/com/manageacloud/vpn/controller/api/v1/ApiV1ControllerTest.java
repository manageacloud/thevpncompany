package com.manageacloud.vpn.controller.api.v1;

import com.manageacloud.vpn.config.VpnConfigurationTests;
import com.manageacloud.vpn.controller.DashboardControllerTest;
import com.manageacloud.vpn.model.*;
import com.manageacloud.vpn.model.vpn.Process;
import com.manageacloud.vpn.repository.CertificateRepository;
import com.manageacloud.vpn.repository.SubscriptionRepository;
import com.manageacloud.vpn.service.ConfigurationService;
import com.manageacloud.vpn.service.PlanService;
import com.manageacloud.vpn.service.UserService;
import com.manageacloud.vpn.utils.CoreUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = VpnConfigurationTests.class)
@TestPropertySource(locations="classpath:vpn-server-test.yml", properties = "thevpncompany.api_key=1234")
public class ApiV1ControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ApiV1ControllerTest.class);

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    UserService userService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void updateUserCertsTests() throws Exception {

        // test3@email.com
        long userId = 3L;

        mockMvc.perform(put("/api/v1/user/" + userId + "/certs")
                .header("apiKey", "1234")
                .param("client_private", "client_private_content")
                .param("client_public", "client_public_content")
                .param("ca", "ca_content")
                .param("static_key", "static_key_content")

        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        User user = userService.findUserById(userId);

        ClientCertificate clientCertificate = certificateRepository.findClientCertificateByUser(user).orElseThrow();

        assertEquals("client_private_content", clientCertificate.getClientPrivate());
        assertEquals("client_public_content", clientCertificate.getClientPublic());
        assertEquals("ca_content", clientCertificate.getCa());
        assertEquals("static_key_content", clientCertificate.getStaticKey());


    }

    @Test
    public void createUserCertsTests() throws Exception  {
        // test3@email.com
        long userId = 4L;

        mockMvc.perform(post("/api/v1/user/" + userId + "/certs")
                .header("apiKey", "1234"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"response\":0}"));

    }

    @Test
    public void createUserDeviceConfigurationsTests()  throws Exception {

        // test5@email.com
        long userId = 5L;

        String expectedWindowsConfiguration = "client\n" +
                "nobind\n" +
                "dev tun\n" +
                "remote-cert-tls server\n" +
                "\n" +
                "remote 5_tests.test-cloud.thevpncompany.com.au 2296 udp\n" +
                "\n" +
                "<key>\n" +
                "private\n" +
                "</key>\n" +
                "<cert>\n" +
                "public\n" +
                "</cert>\n" +
                "<ca>\n" +
                "ca\n" +
                "</ca>\n" +
                "key-direction 1\n" +
                "<tls-auth>\n" +
                "static_key\n" +
                "</tls-auth>\n" +
                "\n" +
                "redirect-gateway def1\n" +
                "\n" +
                "comp-lzo no\n" +
                "\n" +
                "cipher AES-256-CBC\n" +
                "\n";

        mockMvc.perform(post("/api/v1/user/" + userId + "/conf")
                .header("apiKey", "1234"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"response\":0}"));

        User user = userService.findUserById(userId);

        VpnDeviceConfiguration deviceConf = configurationService.findDeviceConfiguration(user, Vpn.Type.OPEN_VPN, Device.Type.WINDOWS);

        assertNotNull(deviceConf);
        assertEquals(expectedWindowsConfiguration, deviceConf.getConfiguration());

        // test update
        mockMvc.perform(post("/api/v1/user/" + userId + "/conf")
                .header("apiKey", "1234"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"response\":0}"));

        deviceConf = configurationService.findDeviceConfiguration(user, Vpn.Type.OPEN_VPN, Device.Type.WINDOWS);

        assertNotNull(deviceConf);
        assertEquals(expectedWindowsConfiguration, deviceConf.getConfiguration());


    }

    @Test
    public void updateUserStatusTest() throws Exception {

        // updateUserStatusTest@email.com
        long userId = 6L;

        User user = userService.findUserById(userId);

        Subscription subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();

        assertEquals(Plan.Type.FREE_500.getId(), subscription.getPlan().getType().getId());
        assertFalse(subscription.isUsed());

        // send an update of bandwidth usage with is lesser than the actual limit
        mockMvc.perform(post("/api/v1/user/" + userId + "/bandwidth")
                .header("apiKey", "1234")
                .param("megabits", "100"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"response\":0}"));

        subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();
        assertFalse(subscription.isUsed());

        mockMvc.perform(get("/api/v1/user/" + userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"access\":0}"));

        // send an update of bandwidth usage with is equals than the actual limit
        mockMvc.perform(post("/api/v1/user/" + userId + "/bandwidth")
                .header("apiKey", "1234")
                .param("megabits", "500")) // notification of full capacity
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"response\":0}"));

        subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();
        assertTrue(subscription.isUsed());

        mockMvc.perform(get("/api/v1/user/" + userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"access\":1}"));

        // after one month, the bandwidth gets reset
        mockMvc.perform(post("/api/v1/user/" + userId + "/bandwidth")
                .header("apiKey", "1234")
                .param("megabits", "0")) // month get reset
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"response\":0}"));

        mockMvc.perform(get("/api/v1/user/" + userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"access\":0}"));


        subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();
        assertFalse(subscription.isUsed());

    }
}