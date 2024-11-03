package com.bca.byc.entity;

import com.bca.byc.entity.impl.SecureIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "role" , indexes = @Index(name = "idx_role_secure_id", columnList = "secure_id", unique = true))
@Entity
public class Role extends AbstractBaseEntity implements Serializable, SecureIdentifiable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3535157084126830747L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    @OneToMany(mappedBy = "role",  fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleHasPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission().getName())).collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority(name));
        return authorities;
    }

    public List<RoleHasPermission> getRolePermission() {
        return permissions;
    }

    public void setRolePermission(List<RoleHasPermission> roleHasPermissions) {
        this.permissions = roleHasPermissions;
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }
}
