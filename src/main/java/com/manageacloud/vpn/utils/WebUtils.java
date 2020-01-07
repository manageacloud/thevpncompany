package com.manageacloud.vpn.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

/**
 * Created by R3Systems Pty Ltd
 * User tk421 on 23/02/16.
 */
public class WebUtils {

    /**
     * The cookie id is a number that we use to track anonymous users
     *
     * @param idCookie
     * @param response
     * @return
     */
    public static String getCookieId(String idCookie, HttpServletResponse response) {
        final int YEAR_SECONDS = 31536000;
        String value;
        if (StringUtils.isEmpty(idCookie)) {
            value = WebUtils.nextSessionId();
            Cookie cookie = new Cookie("id", value);
            cookie.setMaxAge(YEAR_SECONDS);
            response.addCookie(cookie);
        } else {
            value = idCookie;
        }
        return value;
    }
    // Problems mocking static lass
    public static String nextSessionId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    static public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * Adds the headers required for an AMP Redirection
     * Documentation available at https://amp.dev/documentation/components/amp-form/#redirecting-after-a-submission
     *
     * @param uri url to redirect
     */
    static public void ampRedirect(String uri) {

        HttpServletResponse response = CoreUtils.getServerResponse();

        String domain = CoreUtils.getDomain();

        if ( response != null) {
            response.addHeader("AMP-Redirect-To", domain + uri);
            response.addHeader("Access-Control-Expose-Headers", "AMP-Access-Control-Allow-Source-Origin, AMP-Redirect-To");
        }

    }

    static public ModelAndView addDefaultVars(HttpServletRequest request, ModelAndView modelAndView) {
        String pathinfo = Optional.ofNullable(request.getServletPath()).orElse("/");
        modelAndView.addObject("path_info", pathinfo);

        String remoteIP = CoreUtils.getRemoteIp();
        boolean isVPN = CoreUtils.isVPN();
        modelAndView.addObject("isvpn", isVPN);

        modelAndView.addObject("is_dev", CoreUtils.isDevelopment());
        modelAndView.addObject("domain", CoreUtils.getDomain());

        return modelAndView;
    }


}