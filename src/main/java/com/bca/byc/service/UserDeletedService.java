package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.time.LocalDate;
import java.util.Set;

public interface UserDeletedService {

    ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName, LocalDate startDate, LocalDate endDate);

    UserManagementDetailResponse findDataById(Long id) throws BadRequestException;

    void makeUserIsDeletedFalse(Long id);

    void makeUserBulkRestoreTrue(Set<Long> ids);

}
