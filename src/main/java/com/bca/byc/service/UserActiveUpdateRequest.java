package com.bca.byc.service;

import com.bca.byc.entity.Branch;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserActiveUpdateRequest {

    private String name;

    private LocalDate birthDate;

    private String phone;

    private String memberCin;

    private String parentCin;

    private String memberBankAccount;

    private String parentBankAccount;

    private Branch branchCode;

    private String picName;

}
