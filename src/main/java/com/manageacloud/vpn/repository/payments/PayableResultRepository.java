/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.payments;

import com.manageacloud.vpn.model.payment.PayableResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface PayableResultRepository extends JpaRepository<PayableResult, Long> {

    @Query(value = "select u.* from log_transactions u WHERE subscription_id = ?1 ORDER BY created DESC LIMIT 1", nativeQuery = true)
    PayableResult findLastPayableResult(Long subscriptionId);


}
