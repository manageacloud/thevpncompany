/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model.payment;

import com.manageacloud.vpn.model.Subscription;
import com.manageacloud.vpn.model.User;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by R3Systems Pty Ltd
 * User tk421 on 27/11/15.
 */
@Entity
@Table(name = "log_transactions")
public class SubscriptionPayableResult extends PayableResult {

    private SubscriptionPayableResult() {}

    public SubscriptionPayableResult(Subscription subscription, User user, String code, String text, String txtid) {
        super(subscription, user, code, text, txtid);
    }
}
