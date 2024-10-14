package com.bca.byc.service;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.model.AdminCmsDetailResponse;
import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import com.bca.byc.response.AdminPermissionResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AdminService {

    AppAdmin findByEmail(String email);

    AdminDetailResponse findDataById(String id) throws Exception;

    List<AdminDetailResponse> findAllData();

    void saveData(@Valid AdminCreateRequest dto) throws Exception;

    void updateData(String id, @Valid AdminUpdateRequest dto) throws Exception;

    void deleteData(String id) throws Exception;

    ResultPageResponseDTO<AdminDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName);

    AdminCmsDetailResponse getAdminDetail(String email);

    AdminPermissionResponse getPermissionDetail(String email);
}
