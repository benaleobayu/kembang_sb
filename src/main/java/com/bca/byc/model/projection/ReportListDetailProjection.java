package com.bca.byc.model.projection;

import java.time.LocalDateTime;

public interface ReportListDetailProjection {

    String getReporterName();

    String getReason();

    String getOtherReason();

    LocalDateTime getCreatedAt();
}
