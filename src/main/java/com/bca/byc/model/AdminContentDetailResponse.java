package com.bca.byc.model;

import java.util.List;

public record AdminContentDetailResponse(

        String id,

        ChanelResponse chanel,

        List<String> highlight,

        String thumbnail,

        String description,

        List<String> tags,

        String createdBy,

        String updatedBy,

        Boolean status,

        String promotionStatus,

        String promotionPeriod
) {
}
