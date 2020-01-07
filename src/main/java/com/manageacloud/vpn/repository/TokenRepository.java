/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository;

import com.manageacloud.vpn.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByToken(String token);

    @Query(value = "select u.* from tokens u ORDER BY created DESC LIMIT 1", nativeQuery = true)
    Token findLastToken();


}
