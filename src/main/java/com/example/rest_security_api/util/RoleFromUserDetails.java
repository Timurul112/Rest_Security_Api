package com.example.rest_security_api.util;

import org.springframework.security.core.userdetails.UserDetails;

public class RoleFromUserDetails {

    public static String getRole(UserDetails userDetails) {
        if (userDetails != null) {
            return userDetails.getAuthorities().iterator().next().getAuthority();
        } else {
            throw new IllegalArgumentException("UserDetails does not have any GrantedAuthority");
        }
    }
}
