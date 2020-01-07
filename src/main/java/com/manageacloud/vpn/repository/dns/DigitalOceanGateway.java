/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.dns;

import com.manageacloud.vpn.model.Location;
import com.manageacloud.vpn.model.UserLocation;
import com.manageacloud.vpn.model.cloud.Server;
import com.manageacloud.vpn.service.LocationService;
import com.manageacloud.vpn.utils.CoreUtils;
import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Delete;
import com.myjeeva.digitalocean.pojo.DomainRecord;
import com.myjeeva.digitalocean.pojo.DomainRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DigitalOceanGateway {

    @Value( "${vpn.domain.client}" )
    private String domain;

    private static final String CNAME = "CNAME";
    private static final String A = "A";
    private static final Integer TTL = 30;

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    private DigitalOcean apiClient = new DigitalOceanClient(CoreUtils.getEnvironemnt("DNS_DO"));


    public DomainRecord createCNAME(UserLocation userLocation) {

        DomainRecord input = new DomainRecord(userLocation.getUserHostname(), userLocation.getLocation().getHostname() + ".", CNAME, null, null, null, TTL);
        try {
            log.info("Trying to create domain record: " + input.toString());
            DomainRecord domainRecord = apiClient.createDomainRecord(domain, input);
            log.info("Domain Record: " + domainRecord);
            return domainRecord;
        } catch (DigitalOceanException | RequestUnsuccessfulException e) {
            log.error("Exception while creating CNAME: " + e.getMessage(), e);
        }
        return null;

    }


    public DomainRecord updateCNAME(UserLocation userLocation) {

        DomainRecord toReturn = null;

        try {
            DomainRecords domainRecords = apiClient.getDomainRecords(domain, 1, null);
            for (DomainRecord dr : domainRecords.getDomainRecords()) {
                log.info(dr.toString());
                if ( dr.getType().equals(CNAME) && userLocation.getUserHostname().startsWith(dr.getName())) {
                    log.info("Updating " + userLocation.getUserHostname() + "...");
                    DomainRecord record = new DomainRecord(userLocation.getUserHostname(), userLocation.getLocation().getHostname() + ".", CNAME);
                    DomainRecord result = apiClient.updateDomainRecord(domain, dr.getId(), record);
                    log.info("Update Request Object: " + result.toString());
                    toReturn = result;
                    break;
                }
            }
        } catch (DigitalOceanException | RequestUnsuccessfulException e) {
            log.error("Exception while listing CNAME: " + e.getMessage(), e);
        }

        return toReturn;
    }

    public Delete deleteCNAME(UserLocation userLocation) {

        Delete toReturn = null;
        try {
            Delete result = apiClient.deleteDomainRecord(domain, Integer.valueOf(userLocation.getExternal_cname()));
            log.info("Delete Request Object: " + result.toString());
            toReturn = result;
        } catch (DigitalOceanException | RequestUnsuccessfulException e) {
            log.error("Exception while listing CNAME: " + e.getMessage(), e);
        }
        return toReturn;
    }

    /**
     * Find any record that contains the given hostname
     *
     * @param location
     * @return
     */
    public DomainRecord find(Location location) {

        DomainRecord toReturn = null;

        try {
            DomainRecords domainRecords = apiClient.getDomainRecords(domain, 1, null);
            for (DomainRecord dr : domainRecords.getDomainRecords()) {
                log.info(dr.toString());
                if ( location.getHostname().startsWith(dr.getName())) {
                    toReturn = dr;
                    break;
                }
            }
        } catch (DigitalOceanException | RequestUnsuccessfulException e) {
            log.error("Exception while listing CNAME: " + e.getMessage(), e);
        }

        return toReturn;
    }


    public DomainRecord create(Location location, Server server) {

        DomainRecord input = new DomainRecord(location.getHostname(), server.getIpv4(), A, null, null, null, TTL);
        try {
            DomainRecord domainRecord = apiClient.createDomainRecord(domain, input);
            log.info("Domain Record: " + domainRecord);
            return domainRecord;
        } catch (DigitalOceanException | RequestUnsuccessfulException e) {
            log.error("Exception while creating A hostname " + location.getHostname() + ": " + e.getMessage(), e);
        }
        return null;
    }

    public DomainRecord update(Location location, Server server) {

        DomainRecord toReturn = null;

        try {
            DomainRecords domainRecords = apiClient.getDomainRecords(domain, 1, null);
            for (DomainRecord dr : domainRecords.getDomainRecords()) {
                log.info(dr.toString());
                if ( location.getHostname().startsWith(dr.getName())) {
                    log.info("Updating " + location.getHostname() + "...");
                    DomainRecord record = new DomainRecord(location.getHostname(), server.getIpv4(), A);
                    DomainRecord result = apiClient.updateDomainRecord(domain, dr.getId(), record);
                    log.info("Update Request Object: " + result.toString());
                    toReturn = result;
                    break;
                }
            }
        } catch (DigitalOceanException | RequestUnsuccessfulException e) {
            log.error("Exception while updating A: domain: " + domain + " ip: " + server.getIpv4(), e);
        }

        return toReturn;
    }

    public DomainRecord findCNAME(UserLocation userLocation) {

        DomainRecord toReturn = null;

        try {
            DomainRecords domainRecords = apiClient.getDomainRecords(domain, 1, null);
            for (DomainRecord dr : domainRecords.getDomainRecords()) {
                log.info(dr.getName());
                if ( dr.getType().equals(CNAME) && userLocation.getUserHostname().startsWith(dr.getName())) {
                    toReturn = dr;
                    break;
                }
            }
        } catch (DigitalOceanException | RequestUnsuccessfulException e) {
            log.error("Exception while listing CNAME: " + e.getMessage(), e);
        }

        return toReturn;
    }

    public DomainRecord cleanUpAllTests() {

        DomainRecord toReturn = null;

        try {

            DomainRecords domainRecords = apiClient.getDomainRecords(domain, 1, null);
            for (DomainRecord dr : domainRecords.getDomainRecords()) {
                log.info(dr.toString());
                if ( dr.getName().contains("_tests") || dr.getName().contains("-tests")) {
                    log.info("Deleting Testing CNAME record ... ");
                    Delete result = apiClient.deleteDomainRecord(domain, dr.getId());
                    log.info("Deleting Testing CNAME record: " + result.toString());
                }
            }
        } catch (DigitalOceanException | RequestUnsuccessfulException e) {
            log.error("Exception while listing CNAME: " + e.getMessage(), e);
        }

        return toReturn;
    }

}
