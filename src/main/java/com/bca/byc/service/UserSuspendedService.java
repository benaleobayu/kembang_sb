package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.util.Set;

public interface UserSuspendedService {

    ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName);

    UserManagementDetailResponse findDataById(Long id) throws BadRequestException;

    void makeUserIsDeletedTrue(Long id);

    void makeUserIsSuspendedFalse(Long id);

    void makeUserBulkDeleteTrue(Set<Long> ids);

    void makeUserBulkRestoreTrue(Set<Long> ids);
}
