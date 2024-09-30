package com.bca.byc.service;

import com.bca.byc.model.RoleCreateUpdateRequest;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.model.RoleListResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface RoleService {

    RoleDetailResponse findDataBySecureId(String id) throws BadRequestException;

    void saveData(@Valid RoleCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String roleId, @Valid RoleCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;

    ResultPageResponseDTO<RoleListResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    List<String> getAdminRoles();
}

