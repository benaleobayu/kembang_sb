package com.bca.byc.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PreRegisterDetailResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String memberType;
    private String description;
    private String memberBankAccount;
    private String childBankAccount;
    private String memberBirthdate;
    private String childBirthdate;
    private String memberCin;
    private String childCin;

    private String orders;
    private String status;
    private String createdAt;
    private String updatedAt;

}
