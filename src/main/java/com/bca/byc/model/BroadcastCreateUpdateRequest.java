package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BroadcastCreateUpdateRequest {

    private String title;

    private String message;

    @Schema(example = "DRAFT", description = "DRAFT | SENT | SCHEDULED")
    private String status;

    private LocalDateTime postAt;

}
