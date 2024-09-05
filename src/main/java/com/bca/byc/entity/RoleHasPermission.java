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

    public RoleHasPermission(Role data, Permission permission) {
    }

    @Override
    public String toString() {
        return "RoleHasPermission{" +
                "id=" + id +
                ", permission=" + permission.getName() +  // Just get the permission name to avoid recursive loop
                '}';
    }
}
