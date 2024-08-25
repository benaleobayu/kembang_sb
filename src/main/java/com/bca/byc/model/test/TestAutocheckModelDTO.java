package com.bca.byc.model.test;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;


public class TestAutocheckModelDTO {
    @Data
//    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TestAutocheckDetailResponse {

        private String name;
        private String description;
        private String memberType; // get from response
        private String solitaireBankAccount;
        private String solitaireCin; // get from response
        private LocalDate solitaireBirthdate;
        private String memberBankAccount;
        private String memberCin; // get from response
        private LocalDate memberBirthdate;
        private Integer orders;
        private Boolean status;
        private String createdAt;
        private String updatedAt;
    }

    @Data
//    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TestAutocheckCreateRequest implements Serializable {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        private String memberType;

        private String description;

        @NotBlank(message = "Solitaire bank account is mandatory")
        @Size(max = 20, message = "Solitaire bank account must be less than 20 characters")
        private String solitaireBankAccount;

        private String solitaireCin;

        private LocalDate solitaireBirthdate;

        @Size(max = 20, message = "Member bank account must be less than 20 characters")
        private String memberBankAccount;

        private String memberCin;

        private LocalDate memberBirthdate;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;

    }

    @Data
    @AllArgsConstructor
//    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TestAutocheckUpdateRequest {

        @NotBlank(message = "Name is mandatory")
        @Size(max = 50, message = "Name must be less than 50 characters")
        private String name;

        @NotBlank(message = "Member is required")
        private String memberType;

        @NotBlank(message = "Solitaire bank account is mandatory")
        @Size(max = 20, message = "Solitaire bank account must be less than 20 characters")
        private String solitaireBankAccount;

        private LocalDate solitaireBirthdate;

        @NotBlank(message = "Member bank account is mandatory")
        @Size(max = 20, message = "Member bank account must be less than 20 characters")
        private String memberBankAccount;

        private LocalDate memberBirthdate;

        private String description;

        @NotBlank(message = "Order is mandatory")
        private Integer orders;

        @NotBlank(message = "Status is mandatory")
        private Boolean status;
    }


}

