package com.bca.byc.model.projection;

import java.time.LocalDateTime;

public interface ReportContentIndexProjection {
    String getId();

    String getHighlight();

    String getThumbnail();

    String getDescription();

    String getTags();

    String getCreator();

    String getStatusReport();

    Long getTotalReport();

    LocalDateTime getLastReportAt();
}
