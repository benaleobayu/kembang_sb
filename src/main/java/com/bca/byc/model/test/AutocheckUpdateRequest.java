package com.bca.byc.model.test;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AutocheckUpdateRequest {

    private Long id;
    private String name;
    private String description;
    private String memberType; // get from response
    private String memberBankAccount;
    private String memberCin; // get from response
    private LocalDate memberBirthdate;
    private String childBankAccount;
    private String childCin; // get from response
    private LocalDate childBirthdate;
    private Integer orders;
    private Boolean status;

}

