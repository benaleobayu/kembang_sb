package com.bca.byc.model;

import java.util.List;

public record AdminContentIndexResponse<S>(

        String id,

        S index,

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
