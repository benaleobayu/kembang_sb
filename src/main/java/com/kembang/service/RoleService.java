package com.kembang.service;

import com.kembang.model.RoleCreateUpdateRequest;
import com.kembang.model.RoleDetailResponse;
import com.kembang.model.RoleListResponse;
import com.kembang.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.kembang.exception.BadRequestException;

import java.util.List;

public interface RoleService {

    RoleDetailResponse findDataBySecureId(String id) throws BadRequestException;

    void saveData(@Valid RoleCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String roleId, @Valid RoleCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;

    ResultPageResponseDTO<RoleListResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    List<String> getAdminRoles();
}

