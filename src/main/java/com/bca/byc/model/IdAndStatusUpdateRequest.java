package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IdAndStatusUpdateRequest {

    @Schema(description = "UUID", example = "12345678-1234-1234-1234-123456789012")
    private String id;

    @Schema(description = "Type", example = "USER | BUSINESS")
    private String type;
}
