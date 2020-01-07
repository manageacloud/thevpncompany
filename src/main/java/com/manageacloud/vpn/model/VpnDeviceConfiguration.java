/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model;


import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "vpn_devices_configurations")
public class VpnDeviceConfiguration {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="vpn_id", nullable=false)
    private Vpn vpn;

    @ManyToOne
    @JoinColumn(name="device_id", nullable=false)
    private Device device;

    private String configuration;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    protected VpnDeviceConfiguration() {}

    public VpnDeviceConfiguration(User user, Vpn vpn, Device device) {
        this.user = user;
        this.vpn = vpn;
        this.device = device;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Vpn getVpn() {
        return vpn;
    }

    public Device getDevice() {
        return device;
    }

    public String getConfiguration() {
        return configuration;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
