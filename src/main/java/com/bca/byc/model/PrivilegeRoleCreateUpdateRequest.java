package com.bca.byc.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class PrivilegeRoleCreateUpdateRequest {

    private Long permissionId;
    @Getter(AccessLevel.NONE)
    private Boolean active;

    public boolean isActive() {
        return active;
    }
}
