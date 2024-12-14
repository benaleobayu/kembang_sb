package com.kembang.entity.auth;

import com.kembang.entity.AppAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomAdminDetails implements UserDetails {
    private final AppAdmin admin;
    private final List<GrantedAuthority> authorities;

    public CustomAdminDetails(AppAdmin admin, List<GrantedAuthority> authorities) {
        this.admin = admin;
        this.authorities = authorities;
    }

    // Mengembalikan ID admin
    public Long getId() {return admin.getId();}

    public String getSecureId() {return admin.getSecureId();}

    public String getRoleName(){ return admin.getRole().getName();}

    @Override
    public String getUsername() {
        return admin.getEmail();  // Misalnya, gunakan email sebagai username
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return admin.getIsActive();  // Misalnya, status aktif pengguna
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }
}
