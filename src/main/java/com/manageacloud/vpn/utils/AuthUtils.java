package com.manageacloud.vpn.utils;

import com.manageacloud.vpn.model.UserSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    /**
     * Get the email for the logged user
     *
     * @return
     */
    static public String getLoggedEmail() {
        return ((UserSession)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    static public boolean  authenticate (UserSession userSession) {
        if ( userSession != null ) {
            Authentication auth = new UsernamePasswordAuthenticationToken(userSession, null, userSession.getAuthorities());
            AuthUtils.setAuthentication(auth);
            return true;
        } else {
            return false;
        }
    }

    public static void setAuthentication(Authentication authentication) {
        /**
         * Intellij Integration TestsSwitchs crashes randomlnly if this line is executed
         *     SecurityContextHolder.getContext().setAuthentication(authentication);
         */
        if ( !CoreUtils.isJUnitTest() ) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

}
