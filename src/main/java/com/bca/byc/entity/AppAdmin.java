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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar" , columnDefinition = "text")
    private String avatar;

    @Column(name = "cover", columnDefinition = "text")
    private String cover;

    @Column(name = "account_type")
    private String accountType = "Official";

    @Column(name = "is_visible")
    private Boolean isVisible = true;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AdminHasAccounts> adminHasAccounts = new HashSet<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "role_id")
    @EqualsAndHashCode.Exclude
    private Role role;



    // ------------------------------------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }
}
