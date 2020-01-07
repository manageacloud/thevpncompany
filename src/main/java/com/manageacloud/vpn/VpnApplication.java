package com.manageacloud.vpn;

import com.manageacloud.vpn.config.VpnConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(VpnConfiguration.class)
/*@EnableAutoConfiguration(exclude = {FreeMarkerAutoConfiguration.class})*/
public class VpnApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "vpn-server");
        //System.setProperty("spring.config.location, "vpn-server");
        SpringApplication.run(VpnApplication.class, args);
    }

}
