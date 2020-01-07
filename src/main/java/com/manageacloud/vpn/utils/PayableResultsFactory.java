/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.utils;

import com.manageacloud.vpn.model.Subscription;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.payment.CardPayableResult;
import com.manageacloud.vpn.model.payment.CustomerPayableResult;
import com.manageacloud.vpn.model.payment.PayableResult;
import com.manageacloud.vpn.model.payment.SubscriptionPayableResult;
import com.manageacloud.vpn.repository.payments.CardPayableResultRepository;
import com.manageacloud.vpn.repository.payments.CustomerPayableResultRepository;
import com.manageacloud.vpn.repository.payments.PayableResultRepository;
import com.manageacloud.vpn.repository.payments.SubscriptionPayableResultRepository;
import org.springframework.stereotype.Component;

@Component
public class PayableResultsFactory {

    final
    PayableResultRepository payableResultRepository;

    final
    CustomerPayableResultRepository customerPayableResultRepository;

    final
    CardPayableResultRepository cardPayableResultRepository;

    final
    SubscriptionPayableResultRepository subscriptionPayableResultRepository;

    public PayableResultsFactory(PayableResultRepository payableResultRepository, CustomerPayableResultRepository customerPayableResultRepository, CardPayableResultRepository cardPayableResultRepository, SubscriptionPayableResultRepository subscriptionPayableResultRepository) {
        this.payableResultRepository = payableResultRepository;
        this.customerPayableResultRepository = customerPayableResultRepository;
        this.cardPayableResultRepository = cardPayableResultRepository;
        this.subscriptionPayableResultRepository = subscriptionPayableResultRepository;
    }


    public enum Types {
        DEFAULT,
        CARD,
        SUBSCRIPTION,
        CUSTOMER;
    }

    public PayableResult getPayableResult(Types type, Subscription subscription, User user, String code, String text, String txtId ) {

        if ( type == Types.CARD ) {
            return new CardPayableResult(subscription, user, code, text,txtId);
        } else if ( type == Types.SUBSCRIPTION ) {
            return new SubscriptionPayableResult(subscription, user, code, text,txtId);
        } else if ( type == Types.CUSTOMER ) {
            return new CustomerPayableResult(subscription, user, code, text,txtId);
        } else if ( type == Types.DEFAULT ) {
            return new PayableResult(subscription, user, code, text,txtId);
        } else {
            return new PayableResult(subscription, user, code, text,txtId);
        }

    }

}
