package com.manageacloud.vpn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/install")
public class InstallController {

    @RequestMapping("/windows")
    public String windows() {
        return "install/windows";
    }

    @RequestMapping("/mac")
    public String mac() {
        return "install/mac";
    }

    @RequestMapping("/linux")
    public String linux() {
        return "install/linux";
    }

    @RequestMapping("/android")
    public String android() {
        return "install/android";
    }

    @RequestMapping("/ios")
    public String ipad() {
        return "install/ios";
    }

}
