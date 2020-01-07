/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.mock.user;

import com.manageacloud.vpn.model.UserSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

class WithMockUserJohnSmithSecurityContextFactory
            implements WithSecurityContextFactory<WithMockUserJohnSmith> {
        @Override
        public SecurityContext createSecurityContext(WithMockUserJohnSmith customUser) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UserSession principal = new UserSession("john.smith@unittest.com", "password", "John Smith", UserSession.AUTHORITY.ROLE_USER);
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
            context.setAuthentication(auth);
            return context;
        }
    }