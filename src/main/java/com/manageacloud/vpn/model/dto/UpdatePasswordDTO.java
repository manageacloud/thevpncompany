/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdatePasswordDTO implements UpdatePassword {



    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String password2;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPassword2() {
        return password2;
    }

    public boolean areEqual() {
        return password.equals(password2);
    }
}
