package com.bca.byc.model;

import java.util.List;
import java.util.Map;

public record ReportCommentDetailResponse(
        String id,
        String creator,
        String createAt,
        String chanelName,
        Boolean isPublished,
        String description,
        List<String> highlight,
        List<String> tags,
        List<Map<String, String>> contents
) {
}
