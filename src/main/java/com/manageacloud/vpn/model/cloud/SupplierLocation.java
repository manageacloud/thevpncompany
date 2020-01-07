/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model.cloud;


import com.manageacloud.vpn.model.Location;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "supplier_locations")
public class SupplierLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="location_id", nullable=false)
    private Location location;

    @ManyToOne
    @JoinColumn(name="supplier_id", nullable=false)
    private Supplier supplier;

    @Column(name="id_external")
    private String idExternal;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    @Column(insertable = false, updatable = false)
    private Timestamp deleted;

    protected SupplierLocation() {}

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public String getIdExternal() {
        return idExternal;
    }
}
