package com.bca.byc.model;

import java.util.List;

public record CustomerCreateUpdateRequest(
        String name,
        String phone,
        String address,
        String location,
        List<String> daySubscribed,
        Boolean isSubscribed,
        Boolean isActive
) {
}
