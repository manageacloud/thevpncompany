package com.manageacloud.vpn.utils;

import java.sql.Timestamp;

public class DateUtils {

    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

}
