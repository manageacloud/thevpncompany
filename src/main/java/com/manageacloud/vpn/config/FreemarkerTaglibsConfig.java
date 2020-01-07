package com.manageacloud.vpn.config;

import com.manageacloud.vpn.utils.ClassPathTldsLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Workaround to load security tags in freemarker templates
 * https://stackoverflow.com/questions/33755964/spring-boot-with-jsp-tag-libs-in-embedded-tomcat/56095625#56095625
 *
 */
@Configuration
public class FreemarkerTaglibsConfig {


	@Bean
	@ConditionalOnMissingBean(ClassPathTldsLoader.class)
	public ClassPathTldsLoader classPathTldsLoader(){
		return new ClassPathTldsLoader();
	}

}