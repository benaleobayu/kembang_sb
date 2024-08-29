package com.bca.byc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data

public class FaqDetailResponse {

    private Long id;
    private String question;
    private String answer;
    private String description;
    private Integer orders;
    private Boolean status;
    private String createdAt;
    private String updatedAt;
}