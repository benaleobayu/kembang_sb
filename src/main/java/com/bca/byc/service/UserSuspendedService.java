package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.time.LocalDate;
import java.util.Set;

public interface UserSuspendedService {

    ResultPageResponseDTO<UserManagementListResponse> listData(Integer pages,
                                                               Integer limit,
                                                               String sortBy,
                                                               String direction,
                                                               String userName,
                                                               Long locationId,
                                                               LocalDate startDate,
                                                               LocalDate endDate);

    UserManagementDetailResponse findDataBySecureId(String id) throws BadRequestException;

    void makeUserIsDeletedTrue(String id);

    void makeUserIsSuspendedFalse(String id);

    void makeUserBulkDeleteTrue(Set<String> ids);

    void makeUserBulkSuspendedFalse(Set<String> ids);
}
