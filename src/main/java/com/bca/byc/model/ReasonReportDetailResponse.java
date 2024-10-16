package com.bca.byc.model;

public record ReasonReportDetailResponse(
        String id,
        String icon,
        String name,
        Boolean status,
        Integer orders
) {
}
