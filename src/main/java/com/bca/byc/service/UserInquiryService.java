package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface UserInquiryService {

    // view
    UserManagementDetailResponse findDataById(Long id) throws BadRequestException;

    // soft delete
    void softDeleteData(Long id) throws BadRequestException;

    ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName);
}
