package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.Elastic.UserActiveElastic;
import com.bca.byc.model.LogUserManagementRequest;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.model.data.ListTagUserResponse;
import com.bca.byc.response.Page;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;

public interface UserActiveService {

    ResultPageResponseDTO<UserManagementListResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword, Long locationId, LocalDate startDate, LocalDate endDate);

    ResultPageResponseDTO<ListTagUserResponse> listDataTagUser(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    UserManagementDetailResponse findBySecureId(String id) throws BadRequestException;

    void updateData(String id, @Valid UserActiveUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;

    void suspendData(String id, @Valid LogUserManagementRequest dto) throws BadRequestException;

    Page<UserActiveElastic> getAllActiveUser(Integer page, Integer size);

}
