package com.bca.byc.model;

import com.bca.byc.entity.Location;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.UserType;
import lombok.Data;

@Data
public class UserAppDetailResponse {

    private Long id;
    private String name;
    private String email;
    private StatusType status;
    private UserType type;
    private String avatar; // photo
    private String cover; // photo cover profile
    private String biodata;
    private String createdAt;

    private Location location; // TODO: location

//    private List<Business> businesses; // <business>

    private Integer totalFollowers;
    private Integer totalFollowing;
    private Integer totalPosts;
    private Integer totalEvents;

}


