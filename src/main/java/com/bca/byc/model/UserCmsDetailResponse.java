package com.bca.byc.model;

import lombok.Data;

@Data
public class UserCmsDetailResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String type;
    private String memberType;
    private String memberBankAccount;
    private String memberCin;
    private String memberBirthdate;
    private String childBankAccount;
    private String childCin;
    private String childBirthdate;
    private String education;
    private String biodata;
    private String status;
    private String avatar;
    private String cover;
    private String createdAt;
    private String updatedAt;

    private Boolean isRecommended = false;

    private Integer totalFollowers;
    private Integer totalFollowing;

    private Boolean isFollowed = false;
    private Boolean isFollowing = false;

}
