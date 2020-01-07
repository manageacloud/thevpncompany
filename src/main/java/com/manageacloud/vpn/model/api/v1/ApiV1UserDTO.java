package com.manageacloud.vpn.model.api.v1;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ApiV1UserDTO {

    @NotNull
    @NotEmpty
    private String client_private;

    @NotNull
    @NotEmpty
    private String client_public;

    @NotNull
    @NotEmpty
    private String ca;

    @NotNull
    @NotEmpty
    private String static_key;

    public String getClient_private() {
        return client_private;
    }

    public String getClient_public() {
        return client_public;
    }

    public String getCa() {
        return ca;
    }

    public String getStatic_key() {
        return static_key;
    }

    public void setClient_private(String client_private) {
        this.client_private = client_private;
    }

    public void setClient_public(String client_public) {
        this.client_public = client_public;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public void setStatic_key(String static_key) {
        this.static_key = static_key;
    }
}
