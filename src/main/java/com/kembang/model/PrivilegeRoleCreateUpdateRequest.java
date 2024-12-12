package com.kembang.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class PrivilegeRoleCreateUpdateRequest {

    private Long permissionId;
    @Getter(AccessLevel.NONE)
    private Boolean active;

    // Constructor
    public PrivilegeRoleCreateUpdateRequest() {
        this.active = false; // Default value set to false
    }

    public boolean isActive() {
        return active != null ? active : false; // Return false if active is null
    }
}
