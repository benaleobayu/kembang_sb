package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReportUserStatusRequest {

    @Schema(description = "UUID", example = "12345678-1234-1234-1234-123456789012")
    private String id;

    @Schema(description = "Status", example = "SUSPEND | WARNING")
    private String status;

    @Schema(example = "This user is not appropriate")
    private String reason;

}
