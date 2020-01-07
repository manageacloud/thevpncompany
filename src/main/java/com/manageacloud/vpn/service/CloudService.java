/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.service;

import com.manageacloud.vpn.model.Email;
import com.manageacloud.vpn.model.UserLocation;
import com.manageacloud.vpn.model.api.v1.ApiV1ServerActionDTO;
import com.manageacloud.vpn.model.cloud.Server;
import com.manageacloud.vpn.repository.UserLocationRepository;
import com.manageacloud.vpn.repository.cloud.ServerRepository;
import com.manageacloud.vpn.repository.dns.DigitalOceanGateway;
import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.MailUtil;
import com.myjeeva.digitalocean.pojo.DomainRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CloudService {

    private final
    ServerRepository serverRepository;

    private final
    UserLocationRepository userLocationRepository;

    private final
    DigitalOceanGateway digitalOceanGateway;


    private final
    MailUtil mailUtil;

    protected Log log = LogFactory.getLog(this.getClass());

    public CloudService(ServerRepository serverRepository, MailUtil mailUtil, UserLocationRepository userLocationRepository, DigitalOceanGateway digitalOceanGateway) {
        this.serverRepository = serverRepository;
        this.mailUtil = mailUtil;
        this.userLocationRepository = userLocationRepository;
        this.digitalOceanGateway = digitalOceanGateway;
    }

    /**
     * Process actions related with the servers. Potential actions:
     * CreationRequest - the request to create the server has been sent
     * CreationReady - the server has been created and configured successfully
     * CreationFailed - the server creation has failed - server has not been created or server configuration returned error
     *
     * @param serverId
     * @param action
     * @return
     */
    public Server processAction(Long serverId, ApiV1ServerActionDTO action) {

        Optional<Server> optionalServer = serverRepository.findById(serverId);

        Server server = optionalServer.orElse(null);

        if ( server == null ) {
            log.error("Server not found");
            return null;
        }

        if (action.getAction().equals("CreationRequest")) {
            if ( server.getCreationRequest() == null ) {
                server.setCreationRequest();
                server = serverRepository.save(server);
            }

        } else if (action.getAction().equals("CreationReady") && action.getIpv4() != null) {

            if ( server.isBuilding()  ) {
                server.setCreationCompleted();
                server.setIpv4(action.getIpv4());
                server.setId_external(action.getId_external());
                server = serverRepository.save(server);


                // if it is the first server in the location, just make sure that we have
                // the domain record created and that it is pointing to the correct server
                DomainRecord locationDns = digitalOceanGateway.find(server.getSupplierLocation().getLocation());
                if (locationDns == null) {
                    digitalOceanGateway.create(server.getSupplierLocation().getLocation(), server);
                } else {
                    digitalOceanGateway.update(server.getSupplierLocation().getLocation(), server);
                }

                // a user requested the creation of the server. We need to configure the system
                // so this user can start using it.
                if (server.getUser().getId() != -1) {

                    // notify the user who has requested the server
                    Email serverBuilt = Email.builder()
                            .with(CoreUtils.getRemoteIp(), CoreUtils.getCookieId().orElse(""))
                            .fromSupport()
                            .to(server.getUser().getEmail())
                            .subject("Your VPN location is now ready to use")
                            .withTextBody("Thanks for using TheVPNCompany. You have requested to use the location " + server.getSupplierLocation().getLocation().getName() +
                                    " and we have build a new server just for you.")
                            .build();

                    mailUtil.sendEmail(serverBuilt);

                    // change the user location
                    UserLocation userLocation = userLocationRepository.findByUser(server.getUser()).orElseThrow();
                    userLocation.setLocation(server.getSupplierLocation().getLocation());
                    userLocationRepository.save(userLocation);

                    // update DNS
                    digitalOceanGateway.updateCNAME(userLocation);
                }
            }



        } else if (action.getAction().equals("CreationFailed")) {

            server.setCreationFailed();
            server = serverRepository.save(server);

            Email errorEmail = Email.builder()
                    .with(CoreUtils.getRemoteIp(), CoreUtils.getCookieId().orElse(""))
                    .fromSupport()
                    .to(Email.Account.TECH)
                    .subject(CoreUtils.getEnvironemnt() + " - TheVPNCompany Error While Creating Server " + server.getId())
                    .withTextBody("An error has happened while creating the server " + server.getId())
                    .build();

            mailUtil.sendEmail(errorEmail);

        }

        return server;

    }

}
