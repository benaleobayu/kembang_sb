package com.bca.byc.model;

import java.util.List;

public record CustomerDetailResponse(
        String id,
        String name,
        String email,
        String phone,
        String address,
        String locationName,
        List<String> daySubscribed,
        Boolean isSubscribed,
        Boolean isActive
) {
}
