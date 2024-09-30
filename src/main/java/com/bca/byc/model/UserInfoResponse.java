package com.bca.byc.model;

import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import com.bca.byc.model.apps.ExpectCategoryUserInfoResponse;
import com.bca.byc.model.data.BusinessListResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {

    private String id;
    private String name;
    private String email;
    private StatusType status;
    private UserType type;
    private String typeName;
    private String avatar; // photo
    private String cover; // photo cover profile
    private String biodata;
    private List<String> education;
    private String countryCode;
    private String phone;
    private String createdAt;

    private LocationListResponse location; // TODO: location

    private List<BusinessListResponse> businesses; // <business>

    private List<ExpectCategoryUserInfoResponse> expectCategory; // <category>

//    private Integer totalFollowers;
    private Integer totalFollowing;
//    private Integer totalPosts;
//    private Integer totalEvents;

    @Data
    public static class LocationListResponse {
        private Long id;
        private String name;
    }

}


