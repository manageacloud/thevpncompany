package com.manageacloud.vpn.model.api.v1;

import javax.validation.constraints.NotNull;

public class ApiV1UserBandwidthDTO {

    @NotNull
    private Integer megabits;

    public Integer getMegabits() {
        return megabits;
    }

    public void setMegabits(Integer megabits) {
        this.megabits = megabits;
    }
}
