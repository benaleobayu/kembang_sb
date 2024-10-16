package com.bca.byc.model;

import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class ActionMessageRequest {
    @NotBlank(message = "Message is required")
    private String message;
}
