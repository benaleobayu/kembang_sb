package com.kembang.security.util;

import com.kembang.entity.AppUser;
import com.kembang.entity.auth.CustomAdminDetails;
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

        if (principal instanceof CustomAdminDetails) {
            id = ((CustomAdminDetails) principal).getId();  // Ambil ID dari CustomAdminDetails
        } else if (principal instanceof AppUser) {
            id = ((AppUser) principal).getId();
        }

        return id;
    }

    public static String getRoleName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String id = null;

        if (principal instanceof CustomAdminDetails) {
            id = ((CustomAdminDetails) principal).getRoleName();  // Ambil ID dari CustomAdminDetails
        } else if (principal instanceof AppUser) {
            id = null;
        }

        return id;
    }

}
