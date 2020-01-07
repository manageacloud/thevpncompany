/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.interceptor;

import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class APIAuthenticationInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = LoggerFactory.getLogger(APIAuthenticationInterceptor.class);

    @Value("${thevpncompany.api_key}")
    private String vpnCompanyApiKey;

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // API Key required for get PUT POST PATCH
        boolean access = false;

        if ( !request.getMethod().equals("GET") ) {
            if ( request.getHeader("apiKey") != null && !request.getHeader("apiKey").isEmpty() ) {
                String apiKey = request.getHeader("apiKey");
                if ( apiKey.equals(vpnCompanyApiKey) ) {
                    access = true;
                }
            }
        } else {
            access = true;
        }

        if (!access) {
            response.getWriter().write("Access Denied. apikey header required.");
            response.setStatus(401);
        }

        return access;
    }

    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
}
