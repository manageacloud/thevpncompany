package com.manageacloud.vpn.config;

import com.manageacloud.vpn.interceptor.APIAuthenticationInterceptor;
import com.manageacloud.vpn.interceptor.AmpInterceptor;
import com.manageacloud.vpn.interceptor.VPNToolInterceptor;
import com.manageacloud.vpn.utils.CoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.logging.Logger;

/**
 * The user service configuration
 *
 * @author Ruben Rubio
 */
@Configuration
@EntityScan("com.manageacloud.vpn.model")
@ComponentScan(basePackages =  {"com.manageacloud.vpn.controller", "com.manageacloud.vpn.service",
        "com.manageacloud.vpn.utils", "com.manageacloud.vpn.interceptor"})
@EnableJpaRepositories(basePackages = "com.manageacloud.vpn.repository")
@PropertySource("classpath:vpn-db/db-config-vpn.properties")
@PropertySource("classpath:secrets.properties")
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class VpnConfiguration implements WebMvcConfigurer {

    protected Logger logger;

    @Value("${ENV}")
    private String env;

    @Value("${dns.digitalocean_key}")
    private  String digitaloceanKey;

    @Value("${pinnet.api_url}")
    private String pinnetApiUrl;

    @Value("${pinnet.public_key}")
    private String pinnetPublicKey;

    @Value("${pinnet.secret_key}")
    private String pinnetSecretKey;

    @Value("${pinnet.token_monthly_plan}")
    private String pinnetTokenMonthlyPlan;

    @Value("${pinnet.token_6_months_plan}")
    private String pinnetToken6MonthsPlan;

    @Value("${pinnet.token_12_months_plan}")
    private String pinnetToken12MonthsPlan;

    private final APIAuthenticationInterceptor apiAuthenticationInterceptor;

    public VpnConfiguration(APIAuthenticationInterceptor apiAuthenticationInterceptor) {
        logger = Logger.getLogger(getClass().getName());

        this.apiAuthenticationInterceptor = apiAuthenticationInterceptor;
    }

    @Bean
    public void  initializationTest() throws Exception {

        if ( env == null || env.isEmpty() ) {
            throw new IllegalArgumentException("Environment variable ENV needs to be defined");
        } else if ( ! (env.equals("dev") || env.equals("prod") )) {
            throw new IllegalArgumentException("Environment variable ENV value \"" + env + "\" is not correct");
        }

        if ( digitaloceanKey == null || digitaloceanKey.isEmpty() ) {
            throw new IllegalArgumentException("Digital Ocean Key must be defined");
        }

        if ( pinnetApiUrl == null || pinnetApiUrl.isEmpty() ) {
            throw new IllegalArgumentException("Pinnet API URL must be defined");
        }

        if ( pinnetPublicKey == null || pinnetPublicKey.isEmpty() ) {
            throw new IllegalArgumentException("Pinnet Public Key must be defined");
        }

        if ( pinnetSecretKey == null || pinnetSecretKey.isEmpty() ) {
            throw new IllegalArgumentException("Pinnet Secret Key must be defined");
        }

        if ( pinnetTokenMonthlyPlan == null || pinnetTokenMonthlyPlan.isEmpty() ) {
            throw new IllegalArgumentException("Pinnet Monthly Token Plan must be defined");
        }

        if ( pinnetToken6MonthsPlan == null || pinnetToken6MonthsPlan.isEmpty() ) {
            throw new IllegalArgumentException("Pinnet 6 Months Plan Token must be defined");
        }

        if ( pinnetToken12MonthsPlan == null || pinnetToken12MonthsPlan.isEmpty() ) {
            throw new IllegalArgumentException("Pinnet 12 Months Plan Token must be defined");
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VPNToolInterceptor());
        registry.addInterceptor(new AmpInterceptor());
        registry.addInterceptor(apiAuthenticationInterceptor)
                .addPathPatterns("/api/v1/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
        ;
    }
}
