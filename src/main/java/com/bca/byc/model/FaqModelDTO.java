package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class FaqModelDTO {
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FaqDetailResponse {

        private Long id;
        private String name;
        private String description;
        private Integer orders;
        private Boolean status;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FaqCreateRequest {

        @NotBlank(message = "Question is mandatory")
        @Size(min = 3, max = 50, message = "Question must be between 3 and 50 characters")
        private String question;

        @NotBlank(message = "Answer is mandatory")
        @Size(min = 3, max = 50, message = "Answer must be between 3 and 50 characters")
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

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FaqUpdateRequest {

        @NotBlank(message = "Question is mandatory")
        @Size(min = 3, max = 50, message = "Question must be between 3 and 50 characters")
        private String question;

        @NotBlank(message = "Answer is mandatory")
        @Size(min = 3, max = 50, message = "Answer must be between 3 and 50 characters")
        private String answer;

        private String description;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

    }


}
