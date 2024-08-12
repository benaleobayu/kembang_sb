package com.bca.byc.model.api;

import com.bca.byc.model.cms.InterestCategoryModelDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class InterestModelDTO {

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DetailResponse {

        private Long id;

        private String name;

        private String province;

        private String lineOfBusiness;

        private String address;

        private String website;

        private String description;

        private List<InterestCategoryModelDTO.DetailResponse> categories;

        private Integer orders;

        private Boolean status;

        private String createdAt;

        private String updatedAt;

    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CreateRequest {

        @NotBlank(message = "User id is required")
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
    public static class UpdateRequest {

        @NotBlank(message = "User id is required")
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


}