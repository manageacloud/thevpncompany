/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.service;

import com.manageacloud.vpn.model.*;
import com.manageacloud.vpn.model.api.v1.ApiV1UserDTO;
import com.manageacloud.vpn.repository.CertificateRepository;
import com.manageacloud.vpn.repository.DeviceRepository;
import com.manageacloud.vpn.repository.VpnDeviceConfigurationRepository;
import com.manageacloud.vpn.repository.VpnRepository;
import com.manageacloud.vpn.repository.vpn.OpenvpnBackendGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {

    private final
    CertificateRepository certificateRepository;

    private final
    OpenvpnBackendGateway openvpnBackendGateway;

    private final
    DeviceRepository deviceRepository;

    private final
    VpnRepository vpnRepository;

    private final
    VpnDeviceConfigurationRepository vpnDeviceConfigurationRepository;

    @Value( "${vpn.domain.client}" )
    private String domain;

    public ConfigurationService(OpenvpnBackendGateway openvpnBackendGateway, CertificateRepository certificateRepository, DeviceRepository deviceRepository, VpnRepository vpnRepository, VpnDeviceConfigurationRepository vpnDeviceConfigurationRepository) {
        this.openvpnBackendGateway = openvpnBackendGateway;
        this.certificateRepository = certificateRepository;
        this.deviceRepository = deviceRepository;
        this.vpnRepository = vpnRepository;
        this.vpnDeviceConfigurationRepository = vpnDeviceConfigurationRepository;
    }


    /**
     * This is an asynchornous request. The petition is saved in a queue and will
     * be processed by consumer_openvpn_certificates.py
     *
     * @param user
     */
    public void generateConfigurationForClient(User user) {

        // build the configuration on openvpn
        openvpnBackendGateway.generateClient(user);


    }

    /**
     * Save the certificates into the database
     *
     * @param user
     * @param apiV1UserDTO
     * @return
     */
    public ClientCertificate updateCertificates(User user, ApiV1UserDTO apiV1UserDTO) {

        // default VPN technology (OpenVPN)
        Vpn vpn = vpnRepository.findById(Vpn.Type.OPEN_VPN.getId()).orElseThrow();

        // see if the user has already a user cert
        ClientCertificate clientCertificate = certificateRepository.findClientCertificateByUser(user)
                .orElse(new ClientCertificate(vpn, user));

        clientCertificate.setClientPrivate(apiV1UserDTO.getClient_private());
        clientCertificate.setClientPublic(apiV1UserDTO.getClient_public());
        clientCertificate.setCa(apiV1UserDTO.getCa());
        clientCertificate.setStaticKey(apiV1UserDTO.getStatic_key());

        return certificateRepository.save(clientCertificate);

    }


    /**
     * Generate the client configuration for the different devices
     *
     * @param user
     */
    public void generateDeviceConfiguration(User user) {

        // for now it is just OpenVPN
        Vpn vpn = vpnRepository.findById(Vpn.Type.OPEN_VPN.getId()).orElseThrow();

        ClientCertificate clientCertificate = certificateRepository.findClientCertificateByUser(user).orElseThrow();

        List<Device> devices = deviceRepository.findAll();

        for ( Device device : devices ) {

            // create the ovpn file
            Ovpn.Build ovpnBuilder = Ovpn.builder()
                    .withUser(user)
                    .withDomain(domain)
                    .withDevice(device)
                    .withKeys(clientCertificate);


            // customize the ovpn depending on the operating system
            if (device.getId() == Device.Type.LINUX.getId()) {

                ovpnBuilder
                        .withCompress("comp-lzo no")
                        .add("script-security 2")
                        .add("up /etc/openvpn/update-resolv-conf")
                        .add("down /etc/openvpn/update-resolv-conf")
                        .add("sndbuf 0")
                        .add("rcvbuf 0");
            }

            String configuration = ovpnBuilder.build().getConfiguration();

            VpnDeviceConfiguration deviceConf = vpnDeviceConfigurationRepository.findVpnDeviceConfigurationByUserAndDeviceAndVpn(user, device,vpn )
                    .orElse(new VpnDeviceConfiguration(user, vpn, device));

            deviceConf.setConfiguration(configuration);

            vpnDeviceConfigurationRepository.save(deviceConf);


        }
    }


    /**
     * Return the Device configuration for one device.
     *
     * @param user
     * @param vpnType
     * @param deviceType
     * @return
     */
    public VpnDeviceConfiguration findDeviceConfiguration(User user, Vpn.Type vpnType, Device.Type deviceType) {

        Device device = deviceRepository.findById(deviceType.getId()).orElseThrow();
        Vpn vpn = vpnRepository.findById(vpnType.getId()).orElseThrow();

        return vpnDeviceConfigurationRepository.findVpnDeviceConfigurationByUserAndDeviceAndVpn(user, device, vpn).orElse(null);

    }

}
