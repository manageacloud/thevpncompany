/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.config;


import com.manageacloud.vpn.model.Email;
import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.MailUtil;
import com.manageacloud.vpn.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
class GlobalDefaultExceptionHandler {

    @Autowired
    private MailUtil mailUtil;

    @Value( "${email.tech}" )
    private String techEmail;

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e, HttpServletResponse response) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it
        // AnnotationUtils is a Spring Framework utility class.
        //if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
        //  throw e;

        // Otherwise setup and send the user to a default error-view.

        ModelAndView mav = new ModelAndView();
        mav.setViewName(DEFAULT_ERROR_VIEW);
        WebUtils.addDefaultVars(request, mav);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        if (CoreUtils.isProduction() ) {


            String emailBody = CoreUtils.getDebugBody(e);
            String cookie = CoreUtils.getCookieId().orElse("NO_COOKIE");

            Email errorEmail = Email.builder()
                    .with(CoreUtils.getRemoteIp(), cookie)
                    .fromSupport()
                    .to(techEmail)
                    .subject(CoreUtils.getEnvironemnt() + " - TheVPNCompany Error: " + request.getMethod() + " " + request.getRequestURI() + "?" + request.getQueryString())
                    .withTextBody(emailBody)
                    .build();

            mailUtil.sendEmail(errorEmail);


        } else {
            throw e;
        }


        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());


        return mav;
    }
}