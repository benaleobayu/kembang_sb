package com.bca.byc.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }
        String permissionString = permission.toString();
        Collection<? extends SimpleGrantedAuthority> authorities = (Collection<? extends SimpleGrantedAuthority>) authentication.getAuthorities();
        return authorities.stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(permissionString));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, (Object) targetId, permission);
    }
}
