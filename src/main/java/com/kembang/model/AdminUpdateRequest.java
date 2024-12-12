package com.kembang.model;

public record AdminUpdateRequest(
        String email,
        String password,
        String name,
        Boolean status,
        Boolean isVisible, java.util.Set<String> accountIds, String roleId
) {
}

