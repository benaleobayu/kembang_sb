package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class BusinessModelDTO {

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BusinessDetailResponse {

        private Long id;

        private Long userId;

        private String name;

        private String province;

        private String lineOfBusiness;

        private String address;

        private String website;

        private String description;

        private List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> categories;

        private Integer orders;

        private Boolean status;

        private String createdAt;

        private String updatedAt;

    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BusinessCreateRequest {

        @NotBlank(message = "user_id is required")
        private Long userId;

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        private String name;

        private String province;

        private String lineOfBusiness;

        private String address;

        private String website;

        private String description;

        @NotBlank(message = "Order is required")
        private Integer orders;

        @NotBlank(message = "Status is required")
        private Boolean status;

        // many to many
        private List<Long> categoryIds;

    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BusinessUpdateRequest {

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        private String name;

        private String province;

        private String lineOfBusiness;

        private String address;

        private String website;

        private String description;

        @NotBlank(message = "Order is required")
        private Integer orders;

        @NotBlank(message = "Status is required")
        private Boolean status;

        // many to many
        private List<Long> categoryIds;

    }


}
