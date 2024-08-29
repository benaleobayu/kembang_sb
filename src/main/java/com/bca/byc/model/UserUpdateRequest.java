package com.bca.byc.model;


import com.bca.byc.validation.PhoneNumberValidation;
import com.bca.byc.validation.UniqueEmail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@Data

public class UserUpdateRequest {

    private String parentName;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @UniqueEmail
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Size(max = 16, message = "Phone number must be less than 16 characters")
    @PhoneNumberValidation
    private String phone;


    @Size(max = 20, message = "Bank account number must be less than 20 characters")
    private String bankAccount;

    @Size(max = 50, message = "Education must be less than 50 characters")
    private String education;

    @Size(max = 50, message = "Business name must be less than 50 characters")
    private String businessName;

    @Size(max = 20, message = "CIN must be less than 20 characters")
    private String cin;

    private String biodata;

}
