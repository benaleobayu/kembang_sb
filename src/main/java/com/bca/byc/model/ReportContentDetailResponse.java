package com.bca.byc.model;

import com.bca.byc.model.apps.PostContentDetailResponse;

import java.util.List;

public record ReportContentDetailResponse(
        String id,
        String creator,
        String createAt,
        String chanelName,
        Boolean isPublished,
        String description,
        List<String> highlight,
        List<String> tags,
        List<PostContentDetailResponse> content
) {
}
