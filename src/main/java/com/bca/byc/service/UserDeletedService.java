package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.time.LocalDate;
import java.util.Set;

public interface UserDeletedService {

    ResultPageResponseDTO<UserManagementListResponse> listData(Integer pages,
                                                               Integer limit,
                                                               String sortBy,
                                                               String direction,
                                                               String keyword,
                                                               Long locationId, LocalDate startDate,
                                                               LocalDate endDate);

    UserManagementDetailResponse findDataById(String id) throws BadRequestException;

    void makeUserIsDeletedFalse(String id);

    void makeUserBulkRestoreTrue(Set<String> ids);

}
