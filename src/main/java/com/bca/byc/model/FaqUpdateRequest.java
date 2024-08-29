package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data

public class FaqUpdateRequest {

    @NotBlank(message = "Id is mandatory")
    private Integer id;

    @NotBlank(message = "Question is mandatory")
    private String question;

    @NotBlank(message = "Answer is mandatory")
    private String answer;

    private String description;

    @NotBlank(message = "Order is mandatory")
    private Integer orders;

    @NotBlank(message = "Status is mandatory")
    private Boolean status;

    // to relation
    @NotBlank(message = "Faq Category Id is mandatory")
    private Integer faqCategoryId;

}
