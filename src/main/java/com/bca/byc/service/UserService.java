package com.bca.byc.service;

import com.bca.byc.entity.User;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserSetPasswordRequest;
import com.bca.byc.model.api.UserUpdatePasswordRequest;
import com.bca.byc.model.api.UserUpdateRequest;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    boolean existsById(Long userId);

    UserDetailResponse findUserById(Long userId);

    List<UserDetailResponse> findAllUsers();

    void setNewPassword(Long userId, UserSetPasswordRequest dto);

    void changePassword(Long userId, UserUpdatePasswordRequest dto);

    // auth

    void saveUser(RegisterRequest dto) throws Exception;

    void updateUser(Long userId, UserUpdateRequest dto);

    void generateAndSendOtp(User user) throws MessagingException;

    boolean validateOtp(String email, String otpCode);

    void resendOtp(String email) throws MessagingException;
}