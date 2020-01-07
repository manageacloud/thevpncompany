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
import java.util.List;

/**
 * Amp Interceptor sets the Cors headers information as described at
 *
 * https://amp.dev/documentation/guides-and-tutorials/learn/amp-caches-and-cors/amp-cors-requests/
 *
 */
public class AmpInterceptor extends HandlerInterceptorAdapter {

    private static Logger log = LoggerFactory.getLogger(AmpInterceptor.class);


    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (request.getRequestURI().endsWith("/hxr")) {

            String allowedSourceOrigin = CoreUtils.getDomain();

            String origin;
            List<String> allowedOrigins = List.of(allowedSourceOrigin,
                    allowedSourceOrigin + ".cdn.ampproject.org",
                    allowedSourceOrigin + ".amp.cloudflare.com",
                    "http://localhost:8111" // default server
            );
            String sourceOrigin = request.getParameter("__amp_source_origin");
            String headerAmpSameOrigin = request.getHeader("amp-same-origin");
            String headerOrigin = request.getHeader("Origin");

            if (headerAmpSameOrigin != null && headerAmpSameOrigin.equals("true")) {
                origin = sourceOrigin;
            } else if (headerOrigin != null && allowedOrigins.contains(headerOrigin) && sourceOrigin.equals(allowedSourceOrigin)) {
                origin = headerOrigin;
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"unauthorized\"}");
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }

            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", origin);

        }

    }
}
