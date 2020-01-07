package com.manageacloud.vpn.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manageacloud.vpn.utils.CoreUtils;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String username;
    private String password;
    private String loginToken;
    private String email;
    private String phone;
    private String ip;
    private String cookie;

    private Timestamp autopass;

    @JsonIgnore
    @OneToOne(mappedBy="user")
    private UserRole userRole;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    @Column(insertable = false, updatable = false)
    private Timestamp deleted;

    // the session might contain the plain password if the user
    // has been just created
    @Transient
    private String plainPassword;

    private User() {}

    public User(String name, String username, String password, String email, String phone, String ip, Timestamp autopass, String cookie) {
        this.name = name;
        this.cookie = cookie;
        this.username = username;
        this.password = password;
        this.email = email;
        this.ip = ip;
        this.autopass = autopass;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getIp() {
        return ip;
    }

    public String getCookie() {
        return cookie;
    }

    public Timestamp getAutopass() {
        return autopass;
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

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String login_token) {
        this.loginToken = login_token;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public UserSession.AUTHORITY getAuthority() {
        if (this.getUserRole() == null) {
            return UserSession.AUTHORITY.ROLE_USER;
        } else {
            return this.getUserRole().getRole().getAuthority();
        }
    }

    public String getHostname() {
        return id + "_" + CoreUtils.getEnvironemnt() + ".";
    }

}


