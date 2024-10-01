package com.bca.byc.security.util;

import com.bca.byc.entity.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class ContextPrincipal {

    public static String getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email = null;
        if (principal instanceof AppUser) {
            email = ((AppUser) principal).getEmail();
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        }

        return email;
    }
    public static String getSecureUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String secureId = null;
        if (principal instanceof AppUser) {
            secureId =  ((AppUser)principal).getSecureId();
        } else if (principal instanceof UserDetails) {
            secureId = ((UserDetails) principal).getUsername();
        }

        return secureId;
    }

    public static Long getId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Long id = null;
        if (principal instanceof AppUser) {
            id =  ((AppUser)principal).getId();
        } else if (principal instanceof UserDetails) {
            id = Long.parseLong(((UserDetails) principal).getUsername());
        }

        return id;
    }

}
