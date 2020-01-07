/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.utils;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class MyErrorPageRegistrar implements ErrorPageRegistrar {

  @Override
  public void registerErrorPages(ErrorPageRegistry registry) {
      registry.addErrorPages(
              createNotFoundErrorPage());
  }

  private ErrorPage createNotFoundErrorPage() {
      return new ErrorPage(HttpStatus.NOT_FOUND, "/404");
  }

}