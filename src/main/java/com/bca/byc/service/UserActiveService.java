package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.Elastic.UserActiveElastic;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.Page;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface UserActiveService {

    UserManagementDetailResponse findDataById(Long id) throws BadRequestException;

    List<UserManagementDetailResponse> findAllData();

    void updateData(Long id, @Valid UserActiveUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;

    void suspendData(Long id) throws BadRequestException;

    ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName);

    Page<UserActiveElastic> getAllActiveUser(Integer page, Integer size);
}
