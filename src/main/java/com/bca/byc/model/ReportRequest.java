package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class ReportRequest {
    @Schema(example = "POST", description = "POST | COMMENT | COMMENT_REPLY | USER ")
    private String type;

    @Schema(example = "data id")
    private String reportedId;

    @NotBlank(message = "Reason is required")
    @Schema(example = "This comment is not appropriate")
    private String reason;

    @Schema(example = "This optional comment is not appropriate")
    private String otherReason;
}
