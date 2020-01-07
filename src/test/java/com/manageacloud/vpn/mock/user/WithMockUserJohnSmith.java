/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.mock.user;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserJohnSmithSecurityContextFactory.class)
public @interface WithMockUserJohnSmith {

    String username() default "john.smith@unittest.com";

    String name() default "John Smith";
}