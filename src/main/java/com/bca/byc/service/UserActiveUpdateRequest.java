package com.bca.byc.service;

import com.bca.byc.enums.UserType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserActiveUpdateRequest {

    private String name;

    private String email;

    private String phone;

    private LocalDate birthDate;

    private String memberCin;

    private String memberBankAccount;

    private UserType memberType;

    private String parentCin;

    private String parentBankAccount;

    private UserType parentType;

    private String branchId;

    private String picName;

    private Boolean status;

}
