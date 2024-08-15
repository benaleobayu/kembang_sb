package com.bca.byc.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AuthRegisterRequest {

    // user
    private String name;
    private String phone;
    private String email;
    private String type;
    private LocalDate birthdate;
    private LocalDate parentBirthdate;
    // business
    private List<RegisterBusinessRequest> businesses;
    // feedback
    private List<RegisterUserFeedbackRequest> feedbackCategories;

}


