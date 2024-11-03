package com.bca.byc.model;

import java.util.List;

public record CustomerCreateUpdateRequest(
        String name,
        String email,
        String password,
        String phone,
        String address,
        Long location,
        List<String> daySubscribed,
        Boolean isSubscribed,
        Boolean isActive
) {
}
