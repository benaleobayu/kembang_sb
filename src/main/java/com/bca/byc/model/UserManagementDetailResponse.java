package com.bca.byc.model;

import com.bca.byc.enums.StatusType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserManagementDetailResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String type;
    private String memberType;
    private String description;
    private String memberBankAccount;
    private String childBankAccount;
    private String memberCin;
    private String childCin;
//    private String memberBirthdate;
//    private String childBirthdate;

    private String birthDate;

    private String orders;
    private StatusType status;
    private String createdAt;
    private String updatedAt;

}
