package com.bca.byc.model;

import lombok.Data;

@Data
public class RegisterUserFeedbackRequest {
    private Long feedbackCategoryId;
    private String quote;

}
