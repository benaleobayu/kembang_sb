package com.bca.byc.model;

import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {

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


    @Data
    public static class BusinessListResponse {
        private Long id;
        private String name;
        private String lineOfBusiness;
        private Boolean isPrimary;
//        private List<BusinessCategoryResponse> category;
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


