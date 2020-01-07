/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.controller;

import com.manageacloud.vpn.config.VpnConfigurationTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = VpnConfigurationTests.class)
@TestPropertySource(locations="classpath:vpn-server-test.yml")
public class LandingControllerTest {

    private static final Logger log = LoggerFactory.getLogger(LandingControllerTest.class);

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    private CsrfToken csrfToken;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac)
                .build();
        HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        this.csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
    }

//    MvcResult result = mockMvc
//            .perform(get("/").characterEncoding("utf-8"))
//            .andExpect(status().is2xxSuccessful())
//            .andExpect(view().name("index"))
//            //.andDo(print())
//            .andReturn();
//
//    String content = result.getResponse().getContentAsString();

    @Test
    public void homeTest() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"))
            ;
    }

    @Test
    public void whatisVPN()  throws Exception {
        mockMvc
                .perform(get("/what-is-vpn"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("what-is-vpn"))
        ;
    }

    @Test
    public void products()  throws Exception {
        mockMvc
                .perform(get("/products"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("products"))
        ;

    }

    @Test
    public void support()  throws Exception {
        mockMvc
                .perform(get("/support"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("support"))
        ;
    }

    @Test
    public void getStarted()  throws Exception {
        mockMvc
                .perform(get("/get-started").sessionAttr("_csrf", csrfToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("get-started"))
        ;
    }

    @Test
    public void login()  throws Exception {
        mockMvc
                .perform(get("/login") .sessionAttr("_csrf", csrfToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
        ;
    }

    @Test
    public void test404_v1()  throws Exception {
        mockMvc
                .perform(get("/404"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void test404_v2()  throws Exception {
        mockMvc
                .perform(get("/page_does_not_exist"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void sitemap()  throws Exception {
        mockMvc
                .perform(get("/sitemap.xml"))
                .andExpect(status().is2xxSuccessful())
                //.andDo(print())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andExpect(content().string("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" +
                        "  <url>\n" +
                        "    <loc>http://localhost:8111/what-is-vpn</loc>\n" +
                        "  </url>\n" +
                        "  <url>\n" +
                        "    <loc>http://localhost:8111/products</loc>\n" +
                        "  </url>\n" +
                        "  <url>\n" +
                        "    <loc>http://localhost:8111/support</loc>\n" +
                        "  </url>\n" +
                        "  <url>\n" +
                        "    <loc>http://localhost:8111/get-started</loc>\n" +
                        "  </url>\n" +
                        "</urlset>"))
        ;
    }



}