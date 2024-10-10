package com.bca.byc.model;

import java.util.List;

public record AdminContentDetailResponse(

        String id,

        List<String> highlight,

        String thumbnail,

        String description,

        List<String> tags,

        String updatedBy,

        Boolean status,

        String promotionStatus,

        String promotionPeriod
) {
}
