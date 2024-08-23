package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


public class OnboardingModelDTO {
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OnboardingDetailResponse implements Serializable {

        private String name;
        private String description;
        private Integer orders;
        private Boolean status;
        private String createdAt;
        private String updatedAt;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OnboardingCreateRequest {

        // business
        private List<OnboardingBusinessRequest> businesses;
        // expect
        private List<OnboardingExpectCategoryResponse> expectCategories;

        @NotNull(message = "Order is mandatory")
        private Integer orders;

        @NotNull(message = "Status is mandatory")
        private Boolean status;

    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OnboardingUpdateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        private String description;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

    }

    @Data
    public static class OnboardingBusinessRequest {
        private String businessName;
        private String businessAddress;
        private List<OnboardingBusinessCategoryRequest> businessCategories;
        private List<OnboardingLocationRequest> locations;
    }

    @Data
    public static class OnboardingBusinessCategoryRequest {
        private Long businessCategoryId;
        private Long businessCategoryChildId;
    }

    @Data
    public static class OnboardingLocationRequest {
        private Long locationId;
    }


    @Data
    public static class OnboardingExpectCategoryResponse {
        private Long expectCategoryId;
        private Long expectCategoryItemId;
    }

}
