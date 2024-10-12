package com.bca.byc.model;

import java.time.LocalDateTime;
import java.util.List;

public record ReportContentIndexResponse(

        String id,
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
