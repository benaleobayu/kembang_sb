package com.kembang.entity;

import com.kembang.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

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
    public Long getId() {return super.getId();}

    @Override
    public String getSecureId() {return super.getSecureId();}

    @Override
    public Boolean getIsActive() {return super.getIsActive();}

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "location")
    private String location;

    @Column(name = "day_subscribed")
    private String daySubscribed;

    @Column(name = "is_subscribed")
    private Boolean isSubscribed;


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
