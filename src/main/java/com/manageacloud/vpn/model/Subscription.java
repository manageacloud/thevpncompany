package com.manageacloud.vpn.model;


import com.manageacloud.vpn.utils.DateUtils;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="plan_id", nullable=false)
    private Plan plan;

    private String id_external;

    private Timestamp enabled;
    private Timestamp disabled_request;
    private Timestamp disabled_confirmed;

    /**
     * If the subscription is limited by quota, if used flag is set the quote is already met.
     */
    private Timestamp used;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    @Column(insertable = false, updatable = false)
    private Timestamp deleted;

    public Subscription(User user, Plan plan) {
        this.user = user;
        this.plan = plan;
    }

    private Subscription() {}

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Plan getPlan() {
        return plan;
    }

    public String getId_external() {
        return id_external;
    }

    public Timestamp getDisabled_request() {
        return disabled_request;
    }

    public Timestamp getDisabled_confirmed() {
        return disabled_confirmed;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public Timestamp getEnabled() {
        return enabled;
    }

    public Timestamp getUsed() {
        return used;
    }

    public boolean isUsed() {
        return used != null;
    }

    public boolean isEnabled() {
        return (enabled != null && disabled_request == null);
    }

    public void setId_external(String id_external) {
        this.id_external = id_external;
    }

    public void setDisabled_request() {
        this.disabled_request = DateUtils.currentTimestamp();
    }

    public void setDisabled_confirmed() {
        this.disabled_confirmed = DateUtils.currentTimestamp();
    }

    public void setEnabled() {
        this.enabled = DateUtils.currentTimestamp();
    }

    public void setUsed() {
        this.used = DateUtils.currentTimestamp();
    }

    public void setUnused() {
        this.used = null;
    }

}
