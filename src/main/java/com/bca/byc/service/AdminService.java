package com.bca.byc.service;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.model.AdminCmsDetailResponse;
import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import com.bca.byc.response.AdminPermissionResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {

    AppAdmin findByEmail(String email);

    ResultPageResponseDTO<AdminDetailResponse> AdminIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String roleId, Boolean status);

    AdminDetailResponse FindAdminById(String id) throws Exception;

    List<AdminDetailResponse> findAllData();

    void CreateAdmin(@Valid AdminCreateRequest dto, MultipartFile avatar, MultipartFile cover) throws Exception;

    void UpdateAdmin(String id, @Valid AdminUpdateRequest dto, MultipartFile avatar, MultipartFile cover) throws Exception;

    void DeleteAdmin(String id) throws Exception;

    AdminCmsDetailResponse InfoAdmin(String email);

    AdminPermissionResponse DetailAdmin(String email);
}
