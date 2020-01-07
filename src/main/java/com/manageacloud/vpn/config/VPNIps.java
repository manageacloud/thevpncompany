/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.config;

public enum VPNIps {

    US("165.22.159.131"),
    AU("149.28.162.96");

    private String ip;

    VPNIps(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public static boolean isVPN(String remoteIP) {
        for (VPNIps vpnIp : VPNIps.values()) {
            if (vpnIp.getIp().equals(remoteIP)) {
                return true;
            }
        }
        return false;
    }

    public static VPNIps getVpnIUp(String remoteIP) {
        for (VPNIps vpnIp : VPNIps.values()) {
            if (vpnIp.getIp().equals(remoteIP)) {
                return vpnIp;
            }
        }
        return null;
    }

}
