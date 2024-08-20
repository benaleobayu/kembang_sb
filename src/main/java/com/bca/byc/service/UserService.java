package com.bca.byc.service;

import com.bca.byc.entity.User;
import com.bca.byc.model.AuthRegisterRequest;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserSetPasswordRequest;
import com.bca.byc.model.api.UserUpdatePasswordRequest;
import com.bca.byc.model.api.UserUpdateRequest;

import javax.validation.Valid;
import java.util.List;

public interface UserService {

    boolean existsById(Long userId); // check by id
    User findByEmail(String email); // check by email

    UserDetailResponse findDataById(Long userId); // for get 1 data

    List<UserDetailResponse> findAllUsers(); // for get all

    List<UserDetailResponse> findUserPendingAndActive(); // for inquiry get all pending and active

    List<UserDetailResponse> findUserActive(); // for inquiry get all pending and active

    void updateData(Long userId, @Valid UserUpdateRequest dto); // for update data user

    void setNewPassword(String Email, UserSetPasswordRequest dto); // for set new password after login

    void changePassword(Long userId, UserUpdatePasswordRequest dto); // for change password

    void saveData(AuthRegisterRequest dto); // for create user

}