package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record ChangeStatusRequest(

        @NotBlank
        String reportedId,

        @Schema(example = "REVIEW, REJECT, TAKE_DOWN")
        @NotBlank
        String status

) {
}
