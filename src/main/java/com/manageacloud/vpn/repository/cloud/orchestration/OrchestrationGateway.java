/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.repository.cloud.orchestration;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.JobProducer;
import com.google.gson.JsonObject;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.cloud.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Sends the order to create or destroy instances
 * to beanstalkd.
 *
 * This request will be asynchronously processed by those scripts.
 *
 */
@Component
public class OrchestrationGateway {

    @Value( "${tube.server.create}" )
    private String serverCreationQueue;

    @Value( "${beanstalkd.host}" )
    private String host;

    @Value( "${beanstalkd.port}" )
    private int port;


    public void createInstance(User user, Server server) {
        BeanstalkClientFactory factory = this.getFactory();
        JobProducer producer = factory.createJobProducer(serverCreationQueue);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("server_id", server.getId());
        jsonObject.addProperty("supplier_id", server.getSupplierLocation().getSupplier().getId());
        jsonObject.addProperty("location_code", server.getSupplierLocation().getIdExternal());
        jsonObject.addProperty("user_id", user.getId());

        producer.putJob(1000, 0, 3600, jsonObject.toString().getBytes());

        producer.close();
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
