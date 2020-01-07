/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.cloud;

import com.manageacloud.vpn.model.cloud.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
