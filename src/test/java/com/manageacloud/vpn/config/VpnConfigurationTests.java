package com.manageacloud.vpn.config;

import com.manageacloud.vpn.interceptor.APIAuthenticationInterceptor;
import com.manageacloud.vpn.interceptor.VPNToolInterceptor;
import com.manageacloud.vpn.utils.ClassPathTldsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * The user service configuration
 *
 * @author Ruben Rubio
 */
@Configuration
@EnableAutoConfiguration
@EntityScan("com.manageacloud.vpn.model")
@ComponentScan(basePackages =  {"com.manageacloud.vpn.controller", "com.manageacloud.vpn.service",
        "com.manageacloud.vpn.repository.payments", "com.manageacloud.vpn.utils",
        "com.manageacloud.vpn.repository.dns", "com.manageacloud.vpn.repository.vpn",
        "com.manageacloud.vpn.repository.cloud.orchestration",
        "com.manageacloud.vpn.interceptor"})
@EnableJpaRepositories(basePackages = "com.manageacloud.vpn.repository")
@PropertySource("classpath:db-config-vpn-tests.properties")
@PropertySource("classpath:secrets-test.properties")
@PropertySource("classpath:vpn-server-test.yml")
public class VpnConfigurationTests implements WebMvcConfigurer {

    protected Logger logger;

    @Autowired
    private final APIAuthenticationInterceptor apiAuthenticationInterceptor;

    @Value("${spring.datasource.schema-sql}")
    private String schemaSQL;

    @Value("${spring.datasource.data-sql}")
    private String dataSQL;

    @Value("${spring.datasource.test-data-sql}")
    private String testDataSQL;


    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;


    public VpnConfigurationTests(APIAuthenticationInterceptor apiAuthenticationInterceptor) {
        this.apiAuthenticationInterceptor = apiAuthenticationInterceptor;
        logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Rebuilds the test database every time the tests are executed
     */
    @Bean
    public DataSource dataSource() {


        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        populator.addScript(new ClassPathResource(this.schemaSQL));
        populator.addScript(new ClassPathResource(this.dataSQL));
        populator.addScript(new ClassPathResource(this.testDataSQL));

        DataSource dataSource = DataSourceBuilder.create()
                .url(this.url)
                .username(this.username)
                .build();

        DatabasePopulatorUtils.execute(populator, dataSource);

        return dataSource;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VPNToolInterceptor());
        registry.addInterceptor(apiAuthenticationInterceptor)
                .addPathPatterns("/api/v1/**");
    }


    /**
     * Freemarmer configuration
     *
     * @return
     */
    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPaths("classpath:/templates/landing", "classpath:/templates/email");
        return freeMarkerConfigurer;
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(false);
        resolver.setSuffix(".ftl");
        return resolver;
    }

    @Bean
    @ConditionalOnMissingBean(ClassPathTldsLoader.class)
    public ClassPathTldsLoader classPathTldsLoader(){
        return new ClassPathTldsLoader();
    }

    @Bean
    public JavaMailSender mailSender() {
        final JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("os.api.manageacloud.com");
        sender.setPort(2525);
        return sender;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
