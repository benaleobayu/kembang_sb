package com.bca.byc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "role_has_permission")
public class RoleHasPermission {

    @EmbeddedId
    private RoleHasPermissionId id;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;

    public RoleHasPermission() {
        this.id = new RoleHasPermissionId();
    }

    public RoleHasPermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
        this.id = new RoleHasPermissionId(role.getId(), permission.getId()); // Correctly initialize composite key
    }
}

