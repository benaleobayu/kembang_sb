package com.bca.byc.service;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.AppRegisterRequest;
import com.bca.byc.model.UserSetPasswordRequest;
import jakarta.mail.MessagingException;

public interface UserAuthService {

    void saveUser(AppRegisterRequest dto) throws MessagingException;

    boolean validateOtp(String email, String otp);

    void resendOtp(String identity, String email) throws MessagingException;

    void sendRegistrationOtp(String identity, String email) throws MessagingException;


    // generate
    void generateAndSendOtp(String identity, AppUser user) throws MessagingException;


    void setNewPassword(String email, UserSetPasswordRequest dto);
}
