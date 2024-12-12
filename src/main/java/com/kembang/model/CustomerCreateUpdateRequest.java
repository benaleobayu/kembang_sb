package com.kembang.model;

import java.util.List;

public record CustomerCreateUpdateRequest(
        String name,
        String phone,
        String address,
        Integer distance,
        String location,
        List<String> daySubscribed,
        Boolean isSubscribed,
        Boolean isActive
) {
    public CustomerCreateUpdateRequest {
        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        isSubscribed = isSubscribed != null ? isSubscribed : false;
        isActive = isActive != null ? isActive : true;
    }
}