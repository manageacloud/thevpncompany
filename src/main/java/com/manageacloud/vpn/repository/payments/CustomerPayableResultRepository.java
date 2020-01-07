/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.payments;

import com.manageacloud.vpn.model.payment.CustomerPayableResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface CustomerPayableResultRepository extends JpaRepository<CustomerPayableResult, Long> {

}
