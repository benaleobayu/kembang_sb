package com.bca.byc.model;


import com.bca.byc.entity.StatusType;
import lombok.Data;

@Data
public class UserDetailResponse {

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
    private String createdAt;
    private String updatedAt;

    private Boolean isRecommended = false;

    private Integer totalFollowers;
    private Integer totalFollowing;

}
