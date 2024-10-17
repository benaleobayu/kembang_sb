package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ActionMessageRequest;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementFilterList;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.Set;

public interface UserSuspendedService {

    ResultPageResponseDTO<UserManagementListResponse> listData(Integer pages,
                                                               Integer limit,
                                                               String sortBy,
                                                               String direction,
                                                               String keyword,
                                                               UserManagementFilterList filter);

    UserManagementDetailResponse findDataBySecureId(String id) throws BadRequestException;

    void makeUserIsDeletedTrue(String id, @Valid ActionMessageRequest dto);

    void makeUserIsSuspendedFalse(String id);

    void makeUserBulkSuspendedFalse(Set<String> ids);
}
