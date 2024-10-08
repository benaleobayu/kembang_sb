package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FaqCreateUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Question is required")
    private String question;

    @NotBlank(message = "Answer is required")
    private String answer;

    private Boolean status = true;
    private Integer orders;

}
