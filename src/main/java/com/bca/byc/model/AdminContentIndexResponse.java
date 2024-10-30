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

        String promotionPeriod,

        String createBy,

        String createDate,

        Boolean isTeaser

) {

    public AdminContentIndexResponse {
        isTeaser = isTeaser != null && isTeaser;
    }
}
