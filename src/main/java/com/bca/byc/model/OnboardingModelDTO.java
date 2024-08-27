package com.bca.byc.model;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


public class OnboardingModelDTO {
    @Data
    public static class OnboardingDetailResponse implements Serializable {

        private String name;
        private String description;
        private Integer orders;
        private Boolean status;
        private String createdAt;
        private String updatedAt;
    }

    @Data
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
    public static class OnboardingCreateRequest {

        // business
        private List<OnboardingBusinessRequest> businesses;
        // expect
        private List<OnboardingExpectCategoryResponse> expectCategories;

    }

    @Data
    public static class OnboardingBusinessRequest {
        @NotBlank(message = "Business Name is required")
        private String businessName;
        @NotBlank(message = "Business Address is required")
        private String businessAddress;
//        @Valid
//        @NotBlank(message = "Business Category is required")
//        private List<OnboardingBusinessCategoryRequest> businessCategories;
//        @Valid
//        @NotBlank(message = "Business Location is required")
//        private List<OnboardingLocationRequest> locations;
        // additional
        private List<Long> categoryItemIds;
        private List<Long> locationIds;
    }

    @Data
    public static class OnboardingBusinessCategoryRequest {
        @NotBlank(message = "Business Category is required")
        private Long businessCategoryId;
        @NotBlank(message = "Business Category Child is required")
        private Long businessCategoryChildId;
    }

    @Data
    public static class OnboardingLocationRequest {
        @NotBlank(message = "Location is required")
        private Long locationId;
    }

    @Data
    public static class OnboardingExpectCategoryResponse {
        @NotBlank(message = "Expect Category is required")
        private Long expectCategoryId;
//        @NotBlank(message = "Expect Category Item is required")
//        private Long expectCategoryItemId;

        private String otherExpect;
//        private String otherExpectItem;

        private Items items;

        @Data
        public static class Items {
            private List<Long> ids;
            private String otherExpectItem;
        }
    }

}
