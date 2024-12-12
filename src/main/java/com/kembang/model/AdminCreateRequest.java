package com.kembang.model;

import java.util.Set;

public record AdminCreateRequest(
        String email,
        String password,
        String name,
        Boolean status,
        Boolean isVisible,
        Set<String> accountIds,
        String roleId
) {
}

