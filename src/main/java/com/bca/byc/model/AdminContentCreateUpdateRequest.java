package com.bca.byc.model;

import java.time.LocalDateTime;
import java.util.List;

public record AdminContentCreateUpdateRequest(

        Long chanel,

        List<String> highlight,

        String description,

        List<String> tags,

        Boolean status,

        String promotionStatus,

        LocalDateTime promotedAt,

        LocalDateTime promotedUntil,

        LocalDateTime postAt

) {
}
