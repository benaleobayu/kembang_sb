package com.bca.byc.model;

import org.springframework.web.multipart.MultipartFile;

public record ReasonReportCreateUpdateRequest(
        MultipartFile icon,
        String name,
        Integer orders,
        Boolean status,
        Boolean isRequired
        ) {
}
