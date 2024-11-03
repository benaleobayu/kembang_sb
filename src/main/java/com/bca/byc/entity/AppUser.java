package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "app_user", indexes = {@Index(name = "uk_app_user_secure_id", columnList = "secure_id", unique = true)})
public class AppUser extends AbstractBaseEntity implements UserDetails , SecureIdentifiable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 3423593371835697230L;

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Column(name = "name", columnDefinition = "varchar(255) default ''")
    private String name = "";

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;


    // ------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {return null;}

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
}
