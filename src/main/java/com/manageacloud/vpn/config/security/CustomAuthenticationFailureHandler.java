/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.manageacloud.vpn.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
  
    private ObjectMapper objectMapper = new ObjectMapper();
 
    @Override
    public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception)
      throws IOException, ServletException {
  
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        //WebUtils.ampSourceOrigin(request, response);

        Map<String, String> items = Collections.singletonMap("message", "Username and password are not correct");
        String errors = new Gson().toJson(items);
        response.getWriter().write(errors);
        //httpServletResponse.getWriter().flush();
        //httpServletResponse.getWriter().close();
    }
}