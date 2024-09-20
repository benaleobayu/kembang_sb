package com.bca.byc.model.export;

import lombok.Data;

@Data
public class PreRegisterExportResponse {

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
    private String memberBirthdate;
    private String childBirthdate;
}
