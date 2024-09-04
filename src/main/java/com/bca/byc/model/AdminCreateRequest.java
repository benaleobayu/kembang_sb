package com.bca.byc.model;

public record AdminCreateRequest(
        String name,
        String description,
        Integer orders,
        Boolean status
) {
}

