/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model.dto;

import com.manageacloud.vpn.model.Token;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ResetPasswordDTO {

    private Token.Type type = Token.Type.RESET_PASSWORD;

    @Email
    @NotNull
    private String email;

    public String getEmail() {
        return email;
    }

    public Token.Type getType() {
        return type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
