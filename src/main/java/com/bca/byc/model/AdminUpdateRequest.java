package com.bca.byc.model;

public record AdminUpdateRequest(
        String email,
        String password,
        String name,
        Boolean status,
        Boolean isVisible, String roleId
) {
}

