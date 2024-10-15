package com.bca.byc.model;

import lombok.Data;

@Data
public class ReportListDetailResponse {

    String reporterName;
    String reason;
    String otherReason;
    String createdAt;
}
