package com.bca.byc.model.cms;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FaqCategoryModelDTO {
    @Data
    public static class FaqCategoryDetailResponse {

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
    public static class FaqCategoryCreateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
        private String name;

        private String description;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

    }

    @Data
    @AllArgsConstructor
    public static class FaqCategoryUpdateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        private String description;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

    }


}
