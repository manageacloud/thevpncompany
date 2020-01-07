/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository;

import com.manageacloud.vpn.model.ClientCertificate;
import com.manageacloud.vpn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface CertificateRepository extends JpaRepository<ClientCertificate, Long> {

    Optional<ClientCertificate> findClientCertificateByUser(User user);

}
