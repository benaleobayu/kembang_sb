package com.kembang.model;

import java.util.List;

public record CustomerCreateUpdateRequest(
        String name,
        String phone,
        String address,
        String distance,
        String location,
        List<String> daySubscribed,
        Boolean isSubscribed,
        Boolean isActive
) {
    public CustomerCreateUpdateRequest {
        isSubscribed = isSubscribed != null ? isSubscribed : false;
        isActive = isActive != null ? isActive : true;
    }
}