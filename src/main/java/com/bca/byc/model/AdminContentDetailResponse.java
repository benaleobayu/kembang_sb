package com.bca.byc.model;

import java.util.List;

public record AdminContentDetailResponse(

        String id,

        ChannelChecklistResponse channel,

        List<String> highlight,

        String thumbnail,

        List<String> contents,

        String contentType,

        String description,

        List<String> tags,

        String updatedBy,

        Boolean status,

        String promotionStatus,

        String promotionPeriod
) {
}
