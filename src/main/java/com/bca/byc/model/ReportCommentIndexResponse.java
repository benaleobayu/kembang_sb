package com.bca.byc.model;

public record ReportCommentIndexResponse(

        String id,
        Long index,
        String thumbnail,
        String comment,
        String commentOwner,
        String statusReport,
        Integer totalReport,
        String lastReportAt

) {
}
