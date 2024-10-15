package com.bca.byc.model;

import java.util.List;

public record ReportCommentIndexResponse(

        String id,
        Long index,
        List<String> highlight,
        String thumbnail,
        String description,
        String tags,
        String creator,
        String statusReport,
        Long totalReport,
        String lastReportAt

) {
}
