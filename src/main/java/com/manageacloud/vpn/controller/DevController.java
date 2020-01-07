/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.controller;

import com.manageacloud.vpn.model.Email;
import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by R3Systems Pty Ltd
 * User tk421 on 24/06/16.
 */
@SuppressWarnings({"NumericOverflow", "divzero"})
@Controller
@RequestMapping("/dev")
public class DevController {

    private static final Logger log = LoggerFactory.getLogger(DevController.class);

    private final
    MailUtil mailUtil;

    public DevController(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    @RequestMapping(value= "/crash", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String crash(Model model) {
        int i = 1 / 0;
        model.addAttribute("i", i);
        return  "dev/works";
    }


    @RequestMapping(value= "/crash", method = RequestMethod.POST)
    public String crashPost(Model model) {
        int i = 1 / 0;
        model.addAttribute("i", i);
        return  "dev/works";
    }

    @RequestMapping(value= "/view_crash", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String viewCrash() {
        return  "dev/crash";
    }

//    @RequestMapping(value= "/null_crash", method = {RequestMethod.GET, RequestMethod.HEAD})
//    public String nullCrash() {
//        Document document = null;
//        documentRepository.save(document);
//        return  "dev/works";
//    }

    @ResponseBody
    @RequestMapping(value= "/email_test", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String emailTest() {
        String response = "OK";
        String cookie = CoreUtils.getCookieId().orElse("NO_COOKIE");
        Email emailTest = Email.builder()
                .with(CoreUtils.getRemoteIp(), cookie)
                .fromSupport()
                .to(Email.Account.TECH)
                .subject("Test Email")
                .withTextBody("The email body")
                .build();

        mailUtil.sendEmail(emailTest);

        return  response;
    }

    @ResponseBody
    @RequestMapping(value= "/email_test1", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String emailTest1() {
        String response = "OK";
        String cookie = CoreUtils.getCookieId().orElse("NO_COOKIE");
        Email emailTest = Email.builder()
                .with(CoreUtils.getRemoteIp(), cookie)
                .fromSupport()
                .to(Email.Account.TECH)
                .subject("Test Email")
                .withTextBody("The email body")
                .build();

        mailUtil.sendEmail(emailTest);

        return  response;
    }

    @RequestMapping(value= "/test", method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public String test(@RequestParam(value="v", required = false) String isVerbose) {
        int errors = 0;
        int warnings = 0;
        StringBuilder verbose = new StringBuilder();

        if ( CoreUtils.getEnvironemnt() != null ) {

            try {
                // no checks yet

            } catch (Exception e) {
                errors++;
                verbose.append("An exception has occurred: " + e.getMessage() + "\n");
            }
        } else {
            errors ++;
            verbose.append("Environment not defined\n");
        }

        String toReturn;
        if ( isVerbose == null) {
            toReturn = "<!--e:" + errors + "-->\n" +
                    "<!--w:" + warnings + "-->";
        } else {
            toReturn = "<!--e:" + errors + "-->\n" +
                    "<!--w:" + warnings + "-->\n" +
                    "<!--" + verbose.toString() + "-->";;
        }

        return  toReturn;
    }

    private boolean available(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

}
