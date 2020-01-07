/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.config;

import javax.servlet.http.HttpServletRequest;

public enum OS {

    WINDOWS,
    MAC,
    LINUX,
    ANDROID,
    IOS,
    UNKNOWN;

    public static OS valueOf(HttpServletRequest request) {

        final String userAgent = request.getHeader("User-Agent");
        final OS toReturn;

        if (userAgent == null || userAgent.isEmpty()) {
            toReturn = UNKNOWN;
        } else if (userAgent.toLowerCase().contains("windows")) {
            toReturn = WINDOWS;
        } else if (userAgent.toLowerCase().contains("x11")) {
            toReturn = LINUX;
        } else if (userAgent.toLowerCase().contains("android")) {
            toReturn = ANDROID;
        } else if (userAgent.toLowerCase().contains("iphone")) {
            toReturn = IOS;
        } else if (userAgent.toLowerCase().contains("mac")) {
            toReturn = MAC;
        } else {
            toReturn = UNKNOWN;
        }

        return toReturn;
    }

}