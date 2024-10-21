package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestContactUpdateRequest {

    @NotBlank(message = "Message is mandatory")
    private String messages;

    @NotNull(message = "Status is mandatory")
    private Boolean status;
}


