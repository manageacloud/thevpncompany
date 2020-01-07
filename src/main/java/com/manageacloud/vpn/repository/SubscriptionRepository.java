package com.manageacloud.vpn.repository;

import com.manageacloud.vpn.model.Subscription;
import com.manageacloud.vpn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {


    List<Subscription> findAllByUser(User user);

    @Query("select u from Subscription u where u.user = :user AND enabled is not null AND disabled_request is null")
    Optional<Subscription> findEnabledSubscriptionByUser(User user);

    @Query(value = "select u.* from subscriptions u where u.user_id = :user_id ORDER BY created DESC LIMIT 1", nativeQuery = true)
    Subscription findLastSubscription(Long user_id);



}
