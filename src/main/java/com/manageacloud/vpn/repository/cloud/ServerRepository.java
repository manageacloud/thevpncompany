/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.cloud;

import com.manageacloud.vpn.model.cloud.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    /**
     * Get all the servers in a given location that can be used for a VPN connection.
     * We include the servers that are currently building, which are the one that has not failed
     * and has not been requested to be deleted.
     *
     * @param locationId Server's location id
     * @return
     */
    @Query(value = "SELECT u.* " +
            "       FROM servers u " +
            "       WHERE supplier_location_id IN (" +
            "           SELECT id FROM supplier_locations WHERE location_id = :locationId" +
            "       )" +
            "       AND u.creation_failed IS NULL AND u.deletion_request IS NULL" +
            "", nativeQuery = true)
    List<Server> findActiveServersByLocation(@Param("locationId") Long locationId);


    /**
     * Find last record.
     *
     * @return
     */
    Server findFirstByOrderByCreatedDesc();

}
