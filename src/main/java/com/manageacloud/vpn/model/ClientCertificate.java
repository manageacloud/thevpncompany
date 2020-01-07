/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "vpn_user_certs")
public class ClientCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="vpn_id", nullable=false)
    private Vpn vpn;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="private")
    private String clientPrivate;

    @Column(name="public")
    private String clientPublic;

    @Column(name="ca")
    private String ca;

    @Column(name="static_key")
    private String staticKey;

    private Timestamp revoked;

    public ClientCertificate(Vpn vpn, User user, String clientPrivate, String clientPublic, String ca, String staticKey) {
        this.vpn = vpn;
        this.user = user;
        this.clientPrivate = clientPrivate;
        this.clientPublic = clientPublic;
        this.ca = ca;
        this.staticKey = staticKey;
    }

    public ClientCertificate(Vpn vpn, User user) {
        this.vpn = vpn;
        this.user = user;
    }

    private ClientCertificate() {    }

    public long getId() {
        return id;
    }

    public String getClientPrivate() {
        return clientPrivate;
    }

    public String getClientPublic() {
        return clientPublic;
    }

    public String getCa() {
        return ca;
    }

    public String getStaticKey() {
        return staticKey;
    }

    public Timestamp getRevoked() {
        return revoked;
    }

    public Vpn getVpn() {
        return vpn;
    }

    public User getUser() {
        return user;
    }

    public void setClientPrivate(String clientPrivate) {
        this.clientPrivate = clientPrivate;
    }

    public void setClientPublic(String clientPublic) {
        this.clientPublic = clientPublic;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public void setStaticKey(String staticKey) {
        this.staticKey = staticKey;
    }
}
