package com.manageacloud.vpn.controller;

import com.manageacloud.vpn.config.OS;
import com.manageacloud.vpn.service.SitemapService;
import com.manageacloud.vpn.utils.CoreUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Controller
public class LandingController {

    private final SitemapService sitemapService;

    public LandingController(SitemapService sitemapService) {
        this.sitemapService = sitemapService;
    }


    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/amp-form-test")
    public String ampFormTest() {
        return "amp-form-test";
    }


    @RequestMapping("/what-is-vpn")
    public String whatisVPN() {
        return "what-is-vpn";
    }

    @RequestMapping("/products")
    public String products(HttpServletRequest request, Model model) {
        OS os = OS.valueOf(request);
        model.addAttribute("os", os);
        return "products";
    }

    @RequestMapping("/support")
    public String support() {
        return "support";
    }

    @RequestMapping("/support/chat")
    public String chat() {
        return "chat";
    }

    @RequestMapping("/get-started")
    public String getStarted() {
        return "get-started";
    }

    @GetMapping("/login/xhr")
    public String loginXHR() {
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/reset-password")
    public String reset_password() {
        return "reset-password";
    }

    @RequestMapping("/update-password")
    public String update_password(@RequestParam final String token, Model model) {
        model.addAttribute("token", token);
        return "update-password";
    }

    @RequestMapping("/is-vpn-up")
    public String isVpnUp(Model model) {
        model.addAttribute("vpn_ip", CoreUtils.getVpnIp());
        return "is-vpn-up";
    }

    @RequestMapping("/404")
    public String error404(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "error/404";
    }

    @ResponseBody
    @RequestMapping(path = "/sitemap.xml", produces = APPLICATION_XML_VALUE)
    public String create() throws MalformedURLException {
        return sitemapService.createSitemap();
    }


}
