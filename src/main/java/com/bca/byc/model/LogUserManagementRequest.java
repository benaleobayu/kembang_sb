package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LogUserManagementRequest {
    @Schema(example = "SUSPENDED | RESTORE | DELETED")
    private String type;
    @Schema(example = "Have a two accounts")
    private String reason;
}
