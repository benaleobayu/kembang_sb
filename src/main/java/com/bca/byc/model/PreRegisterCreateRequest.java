package com.bca.byc.model;

import com.bca.byc.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PreRegisterCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    @Schema(description = "MEMBER | NOT_CUSTOMER", example = "MEMBER | NOT_CUSTOMER")
    private UserType type;

    @Schema(description = "SOLITAIRE | PRIORITY | NOT_MEMBER", example = "NOT_MEMBER")
    private String memberType;

    private String description;

    private String memberBankAccount;

    private String childBankAccount;

    private LocalDate memberBirthdate;

    private LocalDate childBirthdate;

    private String memberCin;

    private String childCin;

}
