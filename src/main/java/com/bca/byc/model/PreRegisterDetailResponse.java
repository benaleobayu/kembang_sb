package com.bca.byc.model;

import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.StatusType;
import lombok.Data;

@Data
public class PreRegisterDetailResponse {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String type;
    private String memberType;
    private String description;
    private String memberBankAccount;
    private String parentBankAccount;
    private String memberCin;
    private String parentCin;
    private String memberBirthdate;
    private String parentBirthdate;

//    private String birthDate;

    private String orders;
    private AdminApprovalStatus approvalStatus;
    private String createdAt;
    private String updatedAt;

    private String createdBy;
    private String updatedBy;

}
