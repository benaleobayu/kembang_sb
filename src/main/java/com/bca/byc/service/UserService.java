package com.bca.byc.service;

import com.bca.byc.entity.User;
import com.bca.byc.model.*;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.response.ResultPageResponse;

import jakarta.validation.Valid;
import java.util.List;

public interface UserService {

    boolean existsById(Long userId); // check by id
    User findByEmail(String email); // check by email

    User findInfoByEmail(String email);

    UserCmsDetailResponse findDataById(Long userId); // for get 1 data

    List<UserCmsDetailResponse> findAllUsers(); // for get all

    List<UserCmsDetailResponse> findUserPendingAndActive(); // for inquiry get all pending and active

    List<UserCmsDetailResponse> findUserActive(); // for inquiry get all pending and active

    void updateData(Long userId, @Valid UserUpdateRequest dto); // for update data user

    void setNewPassword(String Email, UserSetPasswordRequest dto); // for set new password after login

    void changePassword(Long userId, UserUpdatePasswordRequest dto); // for change password

    void saveData(AuthRegisterRequest dto); // for create user

    // view
    ResultPageResponse<UserCmsDetailResponse> findDataList(
            Integer pages,
            Integer limit,
            String sortBy,
            String direction,
            String userName
    );

    default ResultPageResponse<UserCmsDetailResponse> listFollowUser() {
        return listFollowUser(null, 5, null, null, null);
    }

    ResultPageResponse<UserCmsDetailResponse> listFollowUser(Integer pages, Integer limit, String sortBy, String direction, String userName);


    UserAppDetailResponse getUserDetails(String email);
}