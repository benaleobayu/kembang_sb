package com.bca.byc.entity;


import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "app_admin", indexes = {
    @Index(name = "idx_app_admin_secure_id", columnList = "secure_id", unique = true)
})
public class AppAdmin extends AbstractBaseEntity implements UserDetails, SecureIdentifiable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @EqualsAndHashCode.Exclude
    private Role role;

    // ------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {return role.getAuthorities();}

    @Override
    public String getUsername() {return email;}

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}

    @Override
    public Boolean getIsActive() {return super.getIsActive();}
}
