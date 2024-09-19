package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AppUserProfileRequest {

    private String biography;

    private Long location;

    private List<String> education;

    private List<ProfileExpectCategoryResponse> userHasExpects;

    @Data
    public static class ProfileExpectCategoryResponse {
        @NotBlank(message = "Expect Category is required")
        private Long expectCategoryId;

        private String otherExpect;

        private OnboardingCreateRequest.OnboardingExpectCategoryResponse.Items items;

        @Data
        public static class Items {
            private List<Long> ids;
            private String otherExpectItem;
        }
    }
}
