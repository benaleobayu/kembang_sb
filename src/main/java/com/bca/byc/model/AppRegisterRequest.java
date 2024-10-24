package com.bca.byc.model;

import com.bca.byc.enums.UserType;
import com.bca.byc.validator.annotation.AgeRange;
import com.bca.byc.validator.annotation.UniqueEmail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppRegisterRequest(
        @Schema(description = "john.doe@example.com", example = "john.doe@example.com")
        @NotBlank(message = "email is required")
        String email,

        @Schema(description = "851234567", example = "851234567")
        @Size(max = 15, message = "Phone number must be at least 7 characters")
        String phone,

        @Schema(description = "birthdate of member", example = "1995-01-01")
//        @AgeRange(message = "Member must be at least 18 to 35 years old")
        LocalDate member_birthdate,

        @Schema(description = "1234 5678 9012 3456", example = "1234 5678 9012 3456")
        @Size(max = 25, message = "Card number or account number must be at least 10 characters")
        String member_bank_account,

        @Schema(description = "1234 5678 0000 0000", example = "1234 5678 0000 0000")
        @Size(max = 16, message = "Card number or account number must be at least 16 characters")
        String parent_bank_account
) {
}
