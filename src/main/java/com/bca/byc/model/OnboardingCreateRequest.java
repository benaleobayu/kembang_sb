package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingCreateRequest {
    // business
    private List<OnboardingBusinessRequest> businesses;
    // expect
    private List<OnboardingExpectCategoryResponse> expectCategories;

    @Data
    public static class OnboardingBusinessRequest {
        @NotBlank(message = "Business Name is required")
        private String businessName;
        @NotBlank(message = "Business Address is required")
        private String businessAddress;

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

        private String otherExpect;

        private Items items;

        @Data
        public static class Items {
            private List<Long> ids;
            private String otherExpectItem;
        }
    }
}
