package com.kembang.service;

import com.kembang.entity.AppAdmin;
import com.kembang.model.*;
import com.kembang.response.AdminPermissionResponse;
import com.kembang.response.ResultPageResponseDTO;
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

    AdminCmsDetailResponse InfoAdmin();

    AdminPermissionResponse DetailAdmin(String email);

    void revalidateToken(String token);
}
