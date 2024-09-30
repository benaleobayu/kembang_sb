package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class AppUserProfileRequest {

    private String biography;

    private String location;

    private List<String> education;

    private List<ProfileExpectCategoryResponse> userHasExpects;

    @Data
    public static class ProfileExpectCategoryResponse {

        private String expectCategoryId;

        private String otherExpect;

        private OnboardingCreateRequest.OnboardingExpectCategoryResponse.Items items;

        @Data
        public static class Items {
            private List<String> ids;
            private String otherExpectItem;
        }
    }
}
