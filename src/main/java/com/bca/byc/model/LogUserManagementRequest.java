package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogUserManagementRequest {
    @Schema(example = "SUSPENDED | RESTORE | DELETED")
    private String type;
    @Schema(example = "Have a two accounts")
    private String reason;
}
