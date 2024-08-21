package com.bca.byc.model.auth;

import com.bca.byc.model.OnboardingBusinessRequest;
import com.bca.byc.model.RegisterUserFeedbackRequest;
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
    private String solitaireBankAccount;
    private LocalDate birthdate;
    private LocalDate parentBirthdate;
    // business
    private List<OnboardingBusinessRequest> businesses;
    // feedback
    private List<RegisterUserFeedbackRequest> feedbackCategories;

}


