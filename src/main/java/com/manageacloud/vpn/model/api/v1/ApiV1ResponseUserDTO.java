package com.manageacloud.vpn.model.api.v1;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiV1ResponseUserDTO {

    private int access;

    public ApiV1ResponseUserDTO(int access) {
        this.access = access;
    }

    public int getAccess() {
        return access;
    }
}

