package com.dev.byc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_has_permissions")
public class RoleHasPermissions {

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;

    // Constructors (omitted for brevity)

    public RoleHasPermissions() {
        this.id = new RolePermissionId();
    }

    // Getters and Setters

    public RolePermissionId getId() {
        return id;
    }

    public void setId(RolePermissionId id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
