package com.bca.byc.model.api;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serial;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailResponse {

    private Long id;
    private String parentName;
    private String name;
    private String email;
    private String phone;
    private String solitaireBankAccount;
    private String education;
    private String businessName;
    private String cin;
    private String biodata;
    private String status;
    private String createdAt;
    private String updatedAt;


}
