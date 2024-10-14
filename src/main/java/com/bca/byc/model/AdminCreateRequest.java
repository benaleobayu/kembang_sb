package com.bca.byc.model;

import com.bca.byc.enums.AdminType;

public record AdminCreateRequest(
        String email,
        String password,
        String name,
        Boolean status,
        AdminType type,
        String roleId
) {
}

