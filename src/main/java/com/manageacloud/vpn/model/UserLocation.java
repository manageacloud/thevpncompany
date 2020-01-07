/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model;

import javax.persistence.*;

/**
 * Locations are using the standard ISO 3166-2
 *
 *
 */
@Entity
@Table(name = "user_locations")
public class UserLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="location_id", nullable=false)
    private Location location;

    private String external_cname;

    private UserLocation() {}

    public UserLocation(User user, Location location) {
        this.user = user;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Location getLocation() {
        return location;
    }

    public String getExternal_cname() {
        return external_cname;
    }

    public void setExternal_cname(String external_cname) {
        this.external_cname = external_cname;
    }

    public String getUserHostname() {
        return user.getHostname();
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}