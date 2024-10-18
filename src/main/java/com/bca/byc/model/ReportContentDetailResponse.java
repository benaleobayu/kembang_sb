package com.bca.byc.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record ReportContentDetailResponse(
        String id,
        String creator,
        String createAt,
        String chanelName,
        Boolean isPublished,
        String description,
        List<String> highlight,
        Set<String> tags,
        List<Map<String, String>> contents
) {
}
