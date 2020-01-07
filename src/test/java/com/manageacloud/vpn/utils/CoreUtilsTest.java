/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CoreUtilsTest {

    @Test
    public void encryptPasswordTest() {
        String encryptedPassword = CoreUtils.bcrypt("plainpassword");
        //assertEquals("$2a$11$jX5gxecBBak6EW3ZOb3ZmOHZiNp4ClbobVjp/pi4xp/gRg7TbtI9K", encryptedPassword);
    }

}