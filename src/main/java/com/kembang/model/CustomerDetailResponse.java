package com.kembang.model;

import java.math.BigDecimal;
import java.util.List;

public record CustomerDetailResponse(
        String id,
        String name,
        String email,
        String phone,
        String address,
        String locationName,
        BigDecimal distance,
        List<String> daySubscribed,
        Boolean isSubscribed,
        Boolean isActive
) {
}
