package com.bca.byc.model;

import java.util.List;
import java.util.Map;

public record ReportCommentDetailResponse(
        String id,
        String reportedComments,
        List<Map<String, String>> contents
) {
}
