package com.bca.byc.service;

import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface RoleService {

    RoleDetailResponse findDataById(Long id) throws BadRequestException;

    List<RoleDetailResponse> findAllData();

    void saveData(@Valid RoleCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid RoleUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;

    ResultPageResponseDTO<RoleDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName);

}

