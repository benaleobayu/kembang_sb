package com.bca.byc.model.test;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;


@Data

public class AutocheckDetailResponse {

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

