package com.bca.byc.service;

import com.bca.byc.entity.User;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.model.auth.RegisterRequest;
import jakarta.mail.MessagingException;

public interface AuthService {

    // auth

    void saveUser(RegisterRequest dto) throws Exception; // register

    void generateAndSendOtp(String identity,User user) throws MessagingException;

    void sendWaitingApproval(User user) throws MessagingException;

    boolean validateOtp(String email, String otpCode);

    void resendOtp(String identity, String email) throws MessagingException;
}
