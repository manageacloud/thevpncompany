/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.interceptor;

import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VPNToolInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = LoggerFactory.getLogger(VPNToolInterceptor.class);

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // set the cookie string
        CoreUtils.setCookieIfEmpty();

        return true;
    }

    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if ( modelAndView != null ) {

            WebUtils.addDefaultVars(request, modelAndView);

        }
    }
}
