/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model;


import com.manageacloud.vpn.utils.DateUtils;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tokens")
public class Token {

    public enum Type {
        RESET_PASSWORD;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private String email;
    private String type;
    private String token;

    private Timestamp used;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    @Column(insertable = false, updatable = false)
    private Timestamp deleted;

    // the session might contain the plain password if the user
    // has been just created
    @Transient
    private String plainPassword;

    public Token(String email, Token.Type type, String token) {
        this.email = email;
        this.type = type.toString();
        this.token = token;
    }

    private Token() {}

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public void setUsed() {
        this.used = DateUtils.currentTimestamp();
    }

    public boolean isUsed() {
        return this.used != null;
    }

}


