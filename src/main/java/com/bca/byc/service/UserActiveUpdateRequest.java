package com.bca.byc.service;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserActiveUpdateRequest {

    private String name;

    private LocalDate birthDate;

    private String phone;

    private String memberCin;

    private String childCin;

    private String memberBankAccount;

    private String childBankAccount;

}
