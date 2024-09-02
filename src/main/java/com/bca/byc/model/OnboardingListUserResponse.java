package com.bca.byc.model;

import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.UserType;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingListUserResponse {
    private Long id;
    private String name;
    private String email;
    private StatusType status;
    private UserType type;
    private String avatar; // photo
    private String cover; // photo cover profile
    private String biodata;
    private String createdAt;

    private LocationListResponse location; // TODO: location

    private List<BusinessListResponse> businesses; // <business>

    private Integer totalFollowers;
    private Integer totalFollowing;
    private Integer totalPosts;
    private Integer totalEvents;

    private Boolean isFollowed = false;


    @Data
    public static class BusinessListResponse {
        private Long id;
        private String name;
        private String lineOfBusiness;
        private Boolean isPrimary;
    }

    @Data
    public static class BusinessCategoryResponse {
        private Long id;
        private String name;

    }

    @Data
    public static class LocationListResponse {
        private Long id;
        private String name;
    }
}
