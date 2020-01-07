package com.manageacloud.vpn.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by R3Systems Pty Ltd
 * User tk421 on 5/15/14.
 */
public class UserSession implements UserDetails, Serializable {

    public enum AUTHORITY {
        ROLE_AGENT,
        ROLE_USER,
        ROLE_ADMIN,
    }

    private String username;
    private String password;
    private String name;
    final private AUTHORITY authority;

    public UserSession(String username, String password, String name, AUTHORITY authority) {
        this(username, password, authority);
        this.name = name;
    }


    public UserSession(String username, String password) {
        this(username, password, AUTHORITY.ROLE_USER);
    }

    public UserSession(String username, String password, AUTHORITY authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }



    public Collection<GrantedAuthority> getAuthorities() {
        //make everyone ROLE_USER
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            //anonymous inner type
            public String getAuthority() {
                return authority.toString();
            }
        };
        grantedAuthorities.add(grantedAuthority);
        return grantedAuthorities;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSession that = (UserSession) o;

        return username != null ? username.equals(that.username) : that.username == null;

    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "username='" + username + '\'' +
                ", authority=" + authority +
                '}';
    }
}
