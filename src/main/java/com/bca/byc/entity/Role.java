package com.bca.byc.entity;

import com.github.javafaker.Bool;
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
@Table(name = "role")
@Entity
public class Role extends AbstractBaseEntityNoUUID implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3535157084126830747L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

}
