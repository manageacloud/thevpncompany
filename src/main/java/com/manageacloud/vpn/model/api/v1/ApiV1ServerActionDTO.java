package com.manageacloud.vpn.model.api.v1;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ApiV1ServerActionDTO {

    @NotNull
    @NotEmpty
    private String action;

    private String ipv4;

    private String id_external;

    public ApiV1ServerActionDTO() {
    }

    public ApiV1ServerActionDTO(@NotNull @NotEmpty String action) {
        this.action = action;
    }

    public ApiV1ServerActionDTO(@NotNull @NotEmpty String action, String ipv4, String id_external) {
        this.action = action;
        this.ipv4 = ipv4;
        this.id_external = id_external;
    }

    public String getAction() {
        return action;
    }

    public String getIpv4() {
        return ipv4;
    }

    public String getId_external() {
        return id_external;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public void setId_external(String id_external) {
        this.id_external = id_external;
    }
}
