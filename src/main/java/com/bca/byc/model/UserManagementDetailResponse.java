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
//    private String description;
    private String memberCardNumber;
    private String parentBankAccount;
    private String cin;
//    private String memberBirthdate;
//    private String childBirthdate;

    private String birthDate;

    private Long orders;
    private StatusType status;
    private String createdAt;
    private String updatedAt;

}
