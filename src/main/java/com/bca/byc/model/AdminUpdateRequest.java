package com.bca.byc.model;

public record AdminUpdateRequest(
        Long id,
        String name,
        String description,
        Integer orders,
        Boolean status
) {
}

