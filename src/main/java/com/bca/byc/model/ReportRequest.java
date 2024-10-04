package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReportRequest {
    @Schema(example = "POST or COMMENT or COMMENT_REPLY or USER")
    private String type;

    @Schema(example = "data id")
    private String reportedId;

    @Schema(example = "This comment is not appropriate")
    private String reason;
}
