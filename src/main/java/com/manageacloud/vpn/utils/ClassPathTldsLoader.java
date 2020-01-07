package com.manageacloud.vpn.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Workaround to load security tags in freemarker templates
 * https://stackoverflow.com/questions/33755964/spring-boot-with-jsp-tag-libs-in-embedded-tomcat/56095625#56095625
 *
 */
public class ClassPathTldsLoader  {
	
	private static final String SECURITY_TLD = "/META-INF/security.tld";
	
	final private List<String> classPathTlds;

	public ClassPathTldsLoader(String... classPathTlds) {
	    super();
	    if(ArrayUtils.isEmpty(classPathTlds)){
	    	this.classPathTlds = Arrays.asList(SECURITY_TLD);
	    }else{
	    	this.classPathTlds = Arrays.asList(classPathTlds);
	    }
    }

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@PostConstruct
    public void loadClassPathTlds() {
		freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(classPathTlds);
    }


}