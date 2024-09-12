package com.bca.byc.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AppUserDetailCreateUpdateRequest {

    private String name;

    private String phone;

    private String memberBankAccount;

    private String childBankAccount;

    private LocalDate birthdate;

    private String memberCin;

    private String childCin;

    private String education;

    private String biodata;

    private String memberType;

    private String avatar;

    private String cover;

}
