package com.bca.byc.model;

import java.util.List;
import java.util.Set;

public record ReportContentIndexResponse(

        String id,
        Long index,
        List<String> highlight,
        String thumbnail,
        String description,
        Set<String> tags,
        String creator,
        String reporterEmail,
        Long totalReport,
        String lastReportAt

) {

}
