package com.bca.byc.model.auth;

import com.bca.byc.entity.UserType;
import com.bca.byc.validation.PhoneNumberValidation;
import com.bca.byc.validation.UniqueEmail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must be less than 50 characters")
    @UniqueEmail // Custom annotation for unique email validation
    private String email;

    @Size(max = 16, message = "Phone number must be less than 16 characters")
    private String phone;

    @Schema(description = "User type: (MEMBER, NOT_MEMBER, NOT_CUSTOMER)", example = "'MEMBER' | 'NOT_MEMBER' | 'NOT_CUSTOMER'")
    private UserType type;

    @Size(max = 20, message = "Bank account number must be less than 20 characters")
    private String solitaireBankAccount;

    @Size(max = 20, message = "Customer Identity number must be less than 20 characters")
    private String cin;

    private LocalDate birthdate;

}
