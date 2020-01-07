/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.vpn;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.JobProducer;
import com.google.gson.JsonObject;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.repository.VpnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenvpnBackendGateway {

    @Value( "${tube.cert.create}" )
    private String openVPNCertCreationQueue;

    @Value( "${beanstalkd.host}" )
    private String host;

    @Value( "${beanstalkd.port}" )
    private int port;


    private final
    VpnRepository vpnRepository;

    private static final Logger log = LoggerFactory.getLogger(OpenvpnBackendGateway.class);

    public OpenvpnBackendGateway(VpnRepository vpnRepository) {
        this.vpnRepository = vpnRepository;
    }

    public boolean generateClient(User user) {

        BeanstalkClientFactory factory = this.getFactory();
        JobProducer producer = factory.createJobProducer(openVPNCertCreationQueue);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("common_name", user.getId());

        producer.putJob(1000, 0, 3600, jsonObject.toString().getBytes());

        producer.close();

        return true;

    }

    /**
     * Create the configuration for beanstalk.
     *
     * @return
     */
    private BeanstalkClientFactory getFactory() {
        Configuration config = new Configuration();
        config.setServiceHost(host);
        config.setServicePort(port);
        return new BeanstalkClientFactory(config);
    }

}
