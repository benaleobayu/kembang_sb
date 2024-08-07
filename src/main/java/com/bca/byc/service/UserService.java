package com.bca.byc.service;

import com.bca.byc.entity.User;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.user.UserDetailResponse;
import com.bca.byc.model.user.UserUpdatePasswordRequest;
import com.bca.byc.model.user.UserUpdateRequest;
import jakarta.mail.MessagingException;

import java.util.List;

public interface UserService {


    boolean existsById(Long id);

    boolean validateOtp(String email, String otpCode);

    void generateAndSendOtp(User user) throws MessagingException;

    void resendOtp(String email) throws MessagingException;


    List<User> findAllUsers(int pageable);

    UserDetailResponse getUserDetail(Long userId);

    void saveUser(RegisterRequest registerRequest) throws Exception;

    void updateUser(Long userId, UserUpdateRequest dto);

    void updateUserPassword(Long userId, UserUpdatePasswordRequest dto);


}