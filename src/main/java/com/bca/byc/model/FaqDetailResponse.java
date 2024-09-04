package com.bca.byc.model;

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
