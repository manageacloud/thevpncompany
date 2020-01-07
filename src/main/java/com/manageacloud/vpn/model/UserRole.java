package com.manageacloud.vpn.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by R3Systems Pty Ltd
 * User tk421 on 27/06/16.
 */

@Entity
@Table(name = "user_roles")
public class UserRole implements Serializable {

    public enum Role {
        ROLE_ADMIN,
        ROLE_USER;

        public String toString() {
            return super.toString().toUpperCase();
        }

        public UserSession.AUTHORITY getAuthority() {
            if (this.toString().equals(UserSession.AUTHORITY.ROLE_ADMIN.toString())) {
                return UserSession.AUTHORITY.ROLE_ADMIN;
            } else if (this.toString().equals(UserSession.AUTHORITY.ROLE_USER.toString())) {
                return UserSession.AUTHORITY.ROLE_USER;
            } else {
                throw new UnsupportedOperationException("Role can't be found");
            }
        }
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToOne()
    @JoinColumn(name="user_id", updatable = false)
    private User user;

    private String role;

    protected UserRole() {
    }

    public UserRole(User user, Role role) {
        this.username = user.getEmail();
        this.user = user;
        this.role = role.toString();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return UserRole.Role.valueOf(role);
    }

   @Override
    public String toString() {
        return "UserRole{" +
                "user=" + user +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        return id != null ? id.equals(userRole.id) : userRole.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
