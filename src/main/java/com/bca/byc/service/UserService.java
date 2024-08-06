package com.bca.byc.service;

import com.bca.byc.entity.User;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.user.UserDetail;
import com.bca.byc.model.user.UserUpdateRequest;
import jakarta.mail.MessagingException;

public interface UserService {


    boolean existsById(Long id);

    boolean validateOtp(String email, String otpCode);

    void generateAndSendOtp(User user) throws MessagingException;

    UserDetail getUserDetail(Long userId);

    void resendOtp(String email) throws MessagingException;

    void saveUser(RegisterRequest registerRequest) throws Exception;

    void updateUser(Long userId, UserUpdateRequest dto);


}