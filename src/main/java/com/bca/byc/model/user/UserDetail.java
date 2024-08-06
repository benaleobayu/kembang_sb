package com.bca.byc.model.user;

import lombok.Data;

@Data
public class UserDetail {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String bankAccount;
    private String education;
    private String businessName;
    private String cin;
    private String biodata;
}
