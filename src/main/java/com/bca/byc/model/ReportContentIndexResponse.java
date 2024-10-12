package com.bca.byc.model;

import java.time.LocalDateTime;

public record ReportContentIndexResponse(

        String id,
        String highlight,
        String thumbnail,
        String description,
        String tags,
        String creator,
        String statusReport,
        Integer totalReport,
        LocalDateTime lastReportAt

) {

}
