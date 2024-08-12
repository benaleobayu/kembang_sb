package com.bca.byc.service;

import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserSetPasswordRequest;
import com.bca.byc.model.api.UserUpdatePasswordRequest;
import com.bca.byc.model.api.UserUpdateRequest;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

public interface UserService {

    boolean existsById(Long userId); // check by id

    UserDetailResponse findUserById(Long userId); // for get 1 data

    List<UserDetailResponse> findAllUsers(); // for get all

    void updateUser(Long userId, @Valid UserUpdateRequest dto); // for update data user

    void setNewPassword(Long userId, UserSetPasswordRequest dto); // for set new password after login

    void changePassword(Long userId, UserUpdatePasswordRequest dto); // for change password
}