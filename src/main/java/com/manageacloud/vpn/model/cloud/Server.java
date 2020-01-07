/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model.cloud;


import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.utils.DateUtils;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "servers")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "supplier_location_id", nullable = false)
    private SupplierLocation supplierLocation;

    /**
     *  ID used in the external supplier to identify the resource
     */
    private String id_external;

    /**
     *  Server public IP
     */
    private String ipv4;

    /**
     *  If the server has deletion protection on,
     *  it can not be deleted by the application
     */
    @Column(name="deletion_protection")
    private Timestamp deletionProtection;

    /**
     * Date and time when the server was requested to be built
     */
    @Column(name="creation_request")
    private Timestamp creationRequest;

    /**
     * Date and time when the server was built successfully
     */
    @Column(name="creation_completed")
    private Timestamp creationCompleted;

    /**
     * Date and time when the server creation failed
     */
    @Column(name="creation_failed")
    private Timestamp creationFailed;

    /**
     * Date and time when the server deletion was requested
     */
    @Column(name="deletion_request")
    private Timestamp deletionRequest;

    /**
     * Date and time when the server deletion was completed
     */
    @Column(name="deletion_completed")
    private Timestamp deletionCompleted;

    /**
     * Date and time when the server deletion failed
     */
    @Column(name="deletion_failed")
    private Timestamp deletionFailed;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    @Column(insertable = false, updatable = false)
    private Timestamp deleted;

    private Server() {}

    public Server(User user, SupplierLocation supplierLocation) {
        this.user = user;
        this.supplierLocation = supplierLocation;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public SupplierLocation getSupplierLocation() {
        return supplierLocation;
    }

    public String getId_external() {
        return id_external;
    }

    public String getIpv4() {
        return ipv4;
    }

    public Timestamp getDeletionProtection() {
        return deletionProtection;
    }

    public Timestamp getCreationRequest() {
        return creationRequest;
    }

    public Timestamp getCreationCompleted() {
        return creationCompleted;
    }

    public Timestamp getCreationFailed() {
        return creationFailed;
    }

    public Timestamp getDeletionRequest() {
        return deletionRequest;
    }

    public Timestamp getDeletionCompleted() {
        return deletionCompleted;
    }

    public Timestamp getDeletionFailed() {
        return deletionFailed;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public void setCreationRequest(){
        this.creationRequest = DateUtils.currentTimestamp();
    }

    public void setCreationCompleted(){
        this.creationCompleted = DateUtils.currentTimestamp();
    }

    public void setCreationFailed(){
        this.creationFailed = DateUtils.currentTimestamp();
    }

    public void setDeletionRequest(){
        this.deletionRequest = DateUtils.currentTimestamp();
    }

    public void setDeletionFailed(){
        this.deletionFailed = DateUtils.currentTimestamp();
    }

    public void setDeletionCompleted(){
        this.deletionRequest = DateUtils.currentTimestamp();
    }

    public void setId_external(String id_external) {
        this.id_external = id_external;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public boolean isBuilding() {
        return this.creationRequest != null
                && this.creationCompleted == null
                && this.creationFailed == null
                && this.deletionRequest == null;
    }

    public boolean isActive() {
        return this.creationCompleted != null
                && this.creationFailed == null
                && this.deletionRequest == null;
    }



}


