package com.manageacloud.vpn.repository;

import com.manageacloud.vpn.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

}
