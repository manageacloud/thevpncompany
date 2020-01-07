package com.manageacloud.vpn.model.payment;

import com.manageacloud.vpn.model.Subscription;
import com.manageacloud.vpn.model.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by R3Systems Pty Ltd
 * User tk421 on 27/11/15.
 */
@Entity
@Table(name = "log_transactions")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class PayableResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToOne
    private Subscription subscription;

    private String code;
    private String text;
    private String txtid;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    /**
     * Get's track of the requests and responses for the payable
     *
     * @param subscription subscription related with the request
     * @param user user realted with the request. Most of the time will be the owner fo the subscription but it could also be an admin
     * @param code
     * @param text
     * @param txtid
     */
    public PayableResult(Subscription subscription, User user, String code, String text, String txtid) {
        this.user = user;
        this.subscription = subscription;
        this.code = code;
        this.text = text;
        this.txtid = txtid;
    }

    protected PayableResult() {}

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public String getTxtid() {
        return txtid;
    }

    public User getUser() {
        return user;
    }

    public boolean is2xxCode() {
        return (this.code != null && this.code.startsWith("2"));
    }

//    public Subscription getSubscription() {
//        return subscription;
//    }

    @Override
    public String toString() {
        return "PayableResult{" +
                "code='" + code + '\'' +
                ", text='" + text + '\'' +
                ", txtid='" + txtid + '\'' +
                '}';
    }
}
