/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository;

import com.manageacloud.vpn.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "select u.* from locations u where standard is not null LIMIT 1", nativeQuery = true)
    Location findDefaultLocation();

    Location findLocationByIso(String iso);

    @Query(value = "select u.* from locations u ORDER BY sort ASC", nativeQuery = true)
    List<Location> findLocationsOrderBySort();

}
