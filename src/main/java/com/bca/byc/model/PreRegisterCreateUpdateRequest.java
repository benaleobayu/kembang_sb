package com.bca.byc.model;

import com.bca.byc.enums.UserType;
import com.bca.byc.validator.annotation.UniqueEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PreRegisterCreateUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @UniqueEmail
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @Schema(description = "MEMBER | NOT_MEMBER", example = "MEMBER | NOT_MEMBER")
    private UserType type;

    @NotBlank(message = "Member Card Number is required")
    @Size(min = 16, max = 16, message = "Bank Account must be 16 characters")
    private String memberBankAccount;

    // nullable true
    @Size(max = 10, message = "Bank Account must be 10 characters")
    private String parentBankAccount;

    @NotNull(message = "Member Birthdate is required")
    private LocalDate memberBirthdate;

    // nullable true
    private LocalDate parentBirthdate;

    @NotBlank(message = "Member CIN is required")
    @Size(max = 11, message = "CIN must be 11 characters")
    private String memberCin;

    // nullable true
    @Size(max = 11, message = "CIN must be 11 characters")
    private String parentCin;

    @NotBlank(message = "Branch Code is required")
    private String branchCode;

    @NotBlank(message = "Pic Name is required")
    private String picName;

    private Boolean status;

}
