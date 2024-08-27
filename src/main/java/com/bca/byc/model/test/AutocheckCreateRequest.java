package com.bca.byc.model.test;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AutocheckCreateRequest {

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