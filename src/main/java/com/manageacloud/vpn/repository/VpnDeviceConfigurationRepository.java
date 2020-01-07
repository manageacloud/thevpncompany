/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository;

import com.manageacloud.vpn.model.Device;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.Vpn;
import com.manageacloud.vpn.model.VpnDeviceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Ruben Rubio
 * User tk421 on 27/06/16.
 */
@Repository
public interface VpnDeviceConfigurationRepository extends JpaRepository<VpnDeviceConfiguration, Long> {

    List<VpnDeviceConfiguration> findVpnDeviceConfigurationsByUser(User user);

    Optional<VpnDeviceConfiguration> findVpnDeviceConfigurationByUserAndDeviceAndVpn   (User user, Device device, Vpn vpn);

}
