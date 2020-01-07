/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.service;

import com.manageacloud.vpn.model.Location;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.UserLocation;
import com.manageacloud.vpn.model.cloud.Server;
import com.manageacloud.vpn.model.cloud.SupplierLocation;
import com.manageacloud.vpn.model.dto.LocationDTO;
import com.manageacloud.vpn.repository.LocationRepository;
import com.manageacloud.vpn.repository.UserLocationRepository;
import com.manageacloud.vpn.repository.cloud.ServerRepository;
import com.manageacloud.vpn.repository.cloud.SupplierLocationRepository;
import com.manageacloud.vpn.repository.cloud.orchestration.OrchestrationGateway;
import com.manageacloud.vpn.repository.dns.DigitalOceanGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final
    DigitalOceanGateway digitalOceanGateway;

    private final
    LocationRepository locationRepository;

    private final
    UserLocationRepository userLocationRepository;

    private final
    OrchestrationGateway orchestrationGateway;

    private final
    SupplierLocationRepository supplierLocationRepository;

    private final
    ServerRepository serverRepository;

    public LocationService(DigitalOceanGateway digitalOceanGateway, LocationRepository locationRepository, UserLocationRepository userLocationRepository, OrchestrationGateway orchestrationGateway, SupplierLocationRepository supplierLocationRepository, ServerRepository serverRepository) {
        this.digitalOceanGateway = digitalOceanGateway;
        this.locationRepository = locationRepository;
        this.userLocationRepository = userLocationRepository;
        this.orchestrationGateway = orchestrationGateway;
        this.supplierLocationRepository = supplierLocationRepository;
        this.serverRepository = serverRepository;
    }

    /**
     * Change the user location over DNS.
     *
     * If there are no servers available in the area that the user wants the server
     * the system will request a brand new server.
     *
     * @param user
     * @param locationDTO
     * @return
     */
    public Server updateLocation(User user, LocationDTO locationDTO) {

        // get the new Location
        Location newLocation = locationRepository.findLocationByIso(locationDTO.getIso());

        final Server selectedServer;
        boolean updateLocation = true;
        if ( newLocation.getServer() == null ) { // No default server for location, therefore we have to create a new one

            // make sure that there are no servers in progress
            List<Server> servers = serverRepository.findActiveServersByLocation(newLocation.getId());

            // no servers in progress, so let's request the creation of a new server.
            if ( servers.isEmpty() ) {
                SupplierLocation supplierLocation = supplierLocationRepository.findSupplierLocationByLocation(newLocation);
                selectedServer = new Server(user, supplierLocation);
                serverRepository.save(selectedServer);
                orchestrationGateway.createInstance(user, selectedServer);

                // the server that will be assigned to this user is being created, so we cannot assign the location
                // just yet.
                updateLocation = false;
            } else {
                // In this case, one or several users requested the creation of a server
                // and this server has been already requested and it on progress, so
                // we just pick one of the available servers
                selectedServer = servers.get(0);
            }

        } else { // we have a default server for new clients, so choose that one
            selectedServer = newLocation.getServer();
        }

        if ( updateLocation ) {
            // update the user default's location
            UserLocation userLocation = userLocationRepository.findByUser(user).orElseThrow();
            userLocation.setLocation(newLocation);
            userLocationRepository.save(userLocation);

            // update DNS
            digitalOceanGateway.updateCNAME(userLocation);
        }

        return selectedServer;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findLocationsOrderBySort();
    }

    public UserLocation findUserLocation(User user) {
        return userLocationRepository.findByUser(user).orElseThrow();
    }


}