/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model;

import com.manageacloud.vpn.model.cloud.Server;
import com.manageacloud.vpn.utils.CoreUtils;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Locations are using the standard ISO 3166-2
 *
 *
 */
@Entity
@Table(name = "locations")
public class Location {
    public enum Type  {

        // New South Wales, Australia
        AU_NSW,

        // California, United States
        US_CA,

        // Canada
        CA_ON,

        ;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Every location has a default server where new clients connects to.
     */
    @ManyToOne
    @JoinColumn(name="server_id")
    private Server server;

    private String iso;
    private String name;
    private String hostname;
    private Integer sort;

    @Column(name = "parent_region")
    private String parentRegion;

    private Timestamp standard;

    private Timestamp enabled;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    @Column(insertable = false, updatable = false)
    private Timestamp deleted;

    protected Location() {}

    public long getId() {
        return id;
    }

    public String getIso() {
        return iso;
    }

    public String getName() {
        return name;
    }

    public String getHostname() {

        // support for multiple environments
        final String suffix;
        if (CoreUtils.isProduction()) {
            suffix = "";
        } else if ( CoreUtils.isJUnitTest() ){
            suffix = "-tests";
        } else {
            suffix = "-dev";
        }

        return hostname + suffix;
    }

    public Timestamp getEnabled() {
        return enabled;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public Timestamp getStandard() {
        return standard;
    }

    public Integer getSort() {
        return sort;
    }

    public String getParentRegion() {
        return parentRegion;
    }

    public Server getServer() {
        return server;
    }
}