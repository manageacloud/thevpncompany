/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.controller;

import com.manageacloud.vpn.config.VpnConfigurationTests;
import com.manageacloud.vpn.config.security.WebSecurityConfig;
import com.manageacloud.vpn.mock.user.WithMockUserJohnSmith;
import com.manageacloud.vpn.model.*;
import com.manageacloud.vpn.model.payment.PayableResult;
import com.manageacloud.vpn.model.vpn.Process;
import com.manageacloud.vpn.repository.*;
import com.manageacloud.vpn.repository.dns.DigitalOceanGateway;
import com.manageacloud.vpn.repository.payments.PayableResultRepository;
import com.manageacloud.vpn.utils.CoreUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {VpnConfigurationTests.class, WebSecurityConfig.class})
public class UserControllerTests {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTests.class);

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PayableResultRepository payableResultRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserLocationRepository userLocationRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    DigitalOceanGateway digitalOceanGateway;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    VpnDeviceConfigurationRepository vpnDeviceConfigurationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        // clean up all CNAME tests
        digitalOceanGateway.cleanUpAllTests();

    }


    @Test
    public void createClientTest() throws Exception {

        String userEmail = "workingemail@surfvpn.com.au";

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

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        List<PayableResult> payableResults = payableResultRepository.findAll();

        assertFalse(payableResults.isEmpty());

        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        assertFalse(subscriptions.isEmpty());
        Subscription subscription = subscriptions.get(0);

        assertEquals(Plan.Type.YEAR.getId(), subscription.getPlan().getId());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNotNull(user.getLoginToken());

        // check DNS records
        UserLocation userLocation = userLocationRepository.findByUser(user).orElseThrow();
        assertNotNull(userLocation.getExternal_cname());

        // DNS clean up
        digitalOceanGateway.deleteCNAME(userLocation);

        List<VpnDeviceConfiguration> configurations = vpnDeviceConfigurationRepository.findVpnDeviceConfigurationsByUser(user);
        assertNotNull(configurations);
        for (VpnDeviceConfiguration configuration : configurations) {
            log.info("Configuration: " + configuration);
        }



    }


    @Test
    public void createFreeAccountClientTest() throws Exception {

        String userEmail = "workingemail+500@surfvpn.com.au";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("email", userEmail)
                .param("plan", "5")
                .param("name", "John Smith")
        )
                .andExpect(status().is2xxSuccessful());

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        List<PayableResult> payableResults = payableResultRepository.findAll();

        assertTrue(payableResults.isEmpty()); // is a free account

        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        assertFalse(subscriptions.isEmpty());
        Subscription subscription = subscriptions.get(0);

        assertEquals(Plan.Type.FREE_500.getId(), subscription.getPlan().getId());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNotNull(subscription.getEnabled());
        assertNotNull(user.getLoginToken());

        // check DNS records
        UserLocation userLocation = userLocationRepository.findByUser(user).orElseThrow();
        assertNotNull(userLocation.getExternal_cname());

        // DNS clean up
        digitalOceanGateway.deleteCNAME(userLocation);

        List<VpnDeviceConfiguration> configurations = vpnDeviceConfigurationRepository.findVpnDeviceConfigurationsByUser(user);
        assertNotNull(configurations);
        for (VpnDeviceConfiguration configuration : configurations) {
            log.info("Configuration: " + configuration);
        }



    }



    @Test
    public void createClientWrongCardTest() throws Exception {

        String userEmail = "workingemail@surfvpn.com.au";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("email", userEmail)
                .param("plan", "3")
                .param("cc1", "1234")
                .param("cc2", "1234")
                .param("cc3", "1234")
                .param("cc4", "1234")
                .param("month", "05")
                .param("year", "25")
                .param("cvv", "000")

                .param("name", "John Smith")
                .param("address", "unit 2 / something street ")
                .param("city", "Randwick")
                .param("country", "Australia")

        )
                .andExpect(status().is4xxClientError());

    }



    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void cancelSubscription() throws Exception {

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

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        List<PayableResult> payableResults = payableResultRepository.findAll();

        assertFalse(payableResults.isEmpty());

        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        assertFalse(subscriptions.isEmpty());
        Subscription subscription = subscriptions.get(0);

        assertEquals(Plan.Type.YEAR.getId(), subscription.getPlan().getId());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());

        // check DNS records
        UserLocation userLocation = userLocationRepository.findByUser(user).orElseThrow();
        assertNotNull(userLocation.getExternal_cname());

        // cancel subscription
        mockMvc.perform(post("/dashboard/account/cancel/xhr").with(csrf()))
                .andExpect(status().is2xxSuccessful());

        subscription = subscriptionRepository.findLastSubscription(user.getId());
        assertNotNull(subscription.getDisabled_request());
        assertNotNull(subscription.getDisabled_confirmed());

        userLocation = userLocationRepository.findByUser(user).orElse(null);
        assertNull(userLocation);

    }


    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void changeCreditCard() throws Exception {

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

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        List<PayableResult> payableResults = payableResultRepository.findAll();

        assertFalse(payableResults.isEmpty());

        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        assertFalse(subscriptions.isEmpty());
        Subscription subscription = subscriptions.get(0);

        assertEquals(Plan.Type.YEAR.getId(), subscription.getPlan().getId());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNotNull(subscription.getId_external());

        // update credit card
        mockMvc.perform(post("/dashboard/account/cc/xhr").with(csrf())
                .param("cc1", "4200")
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


    }

    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void changePlanFromPaidToPaidTest() throws Exception {

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

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        List<PayableResult> payableResults = payableResultRepository.findAll();

        assertFalse(payableResults.isEmpty());

        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        assertFalse(subscriptions.isEmpty());
        Subscription subscription = subscriptions.get(0);

        assertEquals(Plan.Type.YEAR.getId(), subscription.getPlan().getId());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNotNull(subscription.getId_external());

        // update credit card
        mockMvc.perform(post("/dashboard/account/subscription/xhr").with(csrf())
                .param("plan", "1")

        )
                .andExpect(status().is2xxSuccessful());

        subscriptions = subscriptionRepository.findAllByUser(user);
        assertEquals(2, subscriptions.size());

        subscription = subscriptionRepository.findLastSubscription(user.getId());

        assertEquals(Plan.Type.MONTHLY.getId(), subscription.getPlan().getId());
        assertNotNull(subscription.getId_external());

    }


    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void changePlanFromFreeToPaidTest() throws Exception {

        // create subscription
        String userEmail = "john.smith@unittest.com";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("name", "John Smith")
                .param("email", userEmail)
                .param("plan", String.valueOf(Plan.Type.FREE_500.getId()))
        )
                .andExpect(status().is2xxSuccessful());

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        Subscription subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();

        assertEquals(Plan.Type.FREE_500.getId(), subscription.getPlan().getId());
        assertTrue(subscription.isEnabled());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNull(subscription.getId_external());

        // update plan to a to 12 months
        mockMvc.perform(post("/dashboard/account/subscription/xhr").with(csrf())
                .param("plan", String.valueOf(Plan.Type.YEAR.getId()))
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

        subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();
        assertEquals(Plan.Type.YEAR.getId(), subscription.getPlan().getId());
        assertTrue(subscription.isEnabled());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNotNull(subscription.getId_external());

    }

    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void changePlanFromPaidToFree() throws Exception {

        // create subscription
        String userEmail = "john.smith@unittest.com";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("email", userEmail)
                .param("plan", String.valueOf(Plan.Type.YEAR.getId()))
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

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        Subscription subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();

        assertEquals(Plan.Type.YEAR.getId(), subscription.getPlan().getId());
        assertTrue(subscription.isEnabled());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNotNull(subscription.getId_external());

        // change plan to free
        mockMvc.perform(post("/dashboard/account/subscription/xhr").with(csrf())
                .param("plan", String.valueOf(Plan.Type.FREE_500.getId()))

        )
                .andExpect(status().is2xxSuccessful());

        subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();
        assertEquals(Plan.Type.FREE_500.getId(), subscription.getPlan().getId());
        assertTrue(subscription.isEnabled());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNull(subscription.getId_external());

    }

    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void changePlanFromFreeToPaidWrongCardTest() throws Exception {

        // create subscription
        String userEmail = "john.smith@unittest.com";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("name", "John Smith")
                .param("email", userEmail)
                .param("plan", String.valueOf(Plan.Type.FREE_500.getId()))
        )
                .andExpect(status().is2xxSuccessful());

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        Subscription subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();

        assertEquals(Plan.Type.FREE_500.getId(), subscription.getPlan().getId());
        assertTrue(subscription.isEnabled());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNull(subscription.getId_external());

        // update plan to a to 12 months
        mockMvc.perform(post("/dashboard/account/subscription/xhr").with(csrf())
                .param("plan", String.valueOf(Plan.Type.YEAR.getId()))
                .param("cc1", "0000") // credit card is wrong
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

        subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();
        assertEquals(Plan.Type.FREE_500.getId(), subscription.getPlan().getId());
        assertTrue(subscription.isEnabled());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNull(subscription.getId_external());

    }


    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void createPlanFromExistingUser() throws Exception {

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

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        List<PayableResult> payableResults = payableResultRepository.findAll();

        assertFalse(payableResults.isEmpty());

        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        assertFalse(subscriptions.isEmpty());
        Subscription subscription = subscriptions.get(0);

        assertEquals(Plan.Type.YEAR.getId(), subscription.getPlan().getId());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());

        // cancel subscription
        mockMvc.perform(post("/dashboard/account/cancel/xhr").with(csrf()))
                .andExpect(status().is2xxSuccessful());

        subscription = subscriptionRepository.findLastSubscription(user.getId());
        assertNotNull(subscription.getDisabled_request());
        assertNotNull(subscription.getDisabled_confirmed());

        // create another subscription
        mockMvc.perform(post("/dashboard/account/subscription/xhr").with(csrf())
                .param("email", userEmail)
                .param("plan", "1")
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

        subscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();

        assertEquals(Plan.Type.MONTHLY.getId(), subscription.getPlan().getId());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());



    }



    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void fromFreeAccounttoPaidAccountTest() throws Exception {

        // create subscription
        String userEmail = "john.smith@unittest.com";

        mockMvc.perform(post("/get-started/xhr").with(csrf())
                .param("plan", String.valueOf(Plan.Type.FREE_500.getId()))
                .param("name", "John Smith")
                .param("email", userEmail)

        )
                .andExpect(status().is2xxSuccessful());

        User user = userRepository.findByEmail(userEmail).get(0);

        assertEquals("John Smith", user.getName());
        assertNotNull(user.getIp());
        assertNull(user.getPhone()); // no phone yet

        List<PayableResult> payableResults = payableResultRepository.findAll();
        assertTrue(payableResults.isEmpty());

        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        assertFalse(subscriptions.isEmpty());
        Subscription subscription = subscriptions.get(0);

        assertEquals(Plan.Type.FREE_500.getId(), subscription.getPlan().getId());
        assertNull(subscription.getDisabled_confirmed());
        assertNull(subscription.getDisabled_request());
        assertNotNull(subscription.getEnabled());

        // create a paid subscription
        mockMvc.perform(post("/dashboard/account/subscription/xhr").with(csrf())
                .param("email", userEmail)
                .param("plan", "1")
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

        // refresh old susbcription
//        subscription = subscriptionRepository.findById(subscription.getId()).orElseThrow();
//        assertNotNull(subscription.getDisabled_request());

        Subscription newSubscription = subscriptionRepository.findEnabledSubscriptionByUser(user).orElseThrow();

        assertEquals(Plan.Type.MONTHLY.getId(), newSubscription.getPlan().getId());
        assertNull(newSubscription.getDisabled_confirmed());
        assertNull(newSubscription.getDisabled_request());



    }


    @Test
    @WithMockUserJohnSmith
    @DirtiesContext
    public void updatePassword() throws Exception {

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

        // cancel subscription
        mockMvc.perform(post("/dashboard/account/password/xhr").with(csrf())
                .param("password", "4556")
                .param("password2", "4556"))
                .andExpect(status().is2xxSuccessful());

        User userNewPassword = userRepository.findOneByEmail(userEmail);

        assertNotEquals(userNewPassword.getPassword(), user.getPassword());

    }


    @Test
    public void loginTest() throws Exception {

        String userEmail = "test@email.com";
        String userPassword = "aaaaaaaa";

        MvcResult mvcResult = mockMvc.perform(post("/login/xhr")
                .with(csrf())
                //.sessionAttr("_csrf", csrfToken)
                .param("username", userEmail)
                .param("password", userPassword)

        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String headerValue = mvcResult.getResponse().getHeader("AMP-Redirect-To");

        assertEquals(CoreUtils.getDomain() + "/dashboard", headerValue);

    }

    @Test
    public void loginFailedTest() throws Exception {

        String userEmail = "test@email.com";
        String userPassword = "wrong_password";

        MvcResult mvcResult =  mockMvc.perform(post("/login/xhr")
                .with(csrf())
                //.sessionAttr("_csrf", csrfToken)
                .param("username", userEmail)
                .param("password", userPassword)

        )
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertEquals(mvcResult.getResponse().getContentAsString(), "{\"message\":\"Username and password are not correct\"}");
    }




    @Test
    public void recoverPasswordTest() throws Exception {

        // create subscription
        String userEmail = "recover_pasword@unittest.com";

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

        // git recover password
        mockMvc.perform(post("/reset-password/xhr").with(csrf())
                .param("email", userEmail)
        )
                .andExpect(status().is2xxSuccessful());

        User oldUser = userRepository.findOneByEmail(userEmail);

        Token token  = tokenRepository.findLastToken();
        assertNotNull(token);
        assertFalse(token.isUsed());

        mockMvc.perform(post("/update-password/xhr").with(csrf())
                .param("token", token.getToken())
                .param("password", "4556")
                .param("password2", "4556")
        )
                .andExpect(status().is2xxSuccessful());

        token  = tokenRepository.findByToken(token.getToken());
        assertTrue(token.isUsed());

        User newUser = userRepository.findOneByEmail(userEmail);
        assertNotEquals(oldUser.getPassword(), newUser.getPassword());
        assertTrue(new BCryptPasswordEncoder(11).matches("4556", newUser.getPassword()));

    }



    @Test
    @DirtiesContext
    public void duplicateUserTest() throws Exception {

        // create subscription
        String userEmail = "duplicated@unittest.com";

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
                .andExpect(status().is4xxClientError());

    }


}
