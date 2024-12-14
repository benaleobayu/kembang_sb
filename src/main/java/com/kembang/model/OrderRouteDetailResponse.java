package com.kembang.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderRouteDetailResponse(
        String id,
        Integer route,
        String driverName,
        LocalDate date,
        BigDecimal totalDistance,
        Integer totalCost,
        Integer totalRemainingCost
) {

}
