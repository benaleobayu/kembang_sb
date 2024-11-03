package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminCmsDetailResponse;
import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.AdminRepository;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.AdminPermissionResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AdminService;
import com.bca.byc.util.FileUploadHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AppAdminRepository appAdminRepository;
    private final AdminRepository repository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Override
    public AppAdmin findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new BadRequestException("Admin not found")
        );
    }

    @Override
    public AdminDetailResponse FindAdminById(String id) throws BadRequestException {
        AppAdmin data = HandlerRepository.getEntityBySecureId(id, repository, "Admin not found");

        return null;
    }

    @Override
    public AdminPermissionResponse DetailAdmin(String email) {
        AppAdmin data = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Admin not found"));

        AdminPermissionResponse dto = new AdminPermissionResponse();

        List<String> permissions = new ArrayList<>();

        for (RoleHasPermission roleHasPermission : data.getRole().getRolePermission()) {
            String[] parts = roleHasPermission.getPermission().getName().split("\\.");
            if (parts.length > 1) {
                String category = parts[0];
                String permission = parts[1];
                permissions.add(category + "." + permission);
            }
        }

        dto.setPermissions(permissions);
        return dto;
    }

    @Override
    public List<AdminDetailResponse> findAllData() {
        // Get the list
        List<AppAdmin> datas = repository.findAll();

        // stream into the list
        return null;
    }

    @Override
    @Transactional
    public void CreateAdmin(@Valid AdminCreateRequest dto, MultipartFile avatar, MultipartFile cover) throws BadRequestException, IOException {
        FileUploadHelper.validateFileTypeImage(avatar);
        // set entity to add with model mapper
        AppAdmin data = new AppAdmin();
        data.setName(dto.name());
        data.setEmail(dto.email());
        data.setPassword(passwordEncoder.encode(dto.password()));
        Role role = HandlerRepository.getEntityBySecureId(dto.roleId(), roleRepository, "Role not found");
        data.setRole(role);
        data.setIsActive(dto.status());

        // save data
        AppAdmin savedAdmin = repository.save(data);
    }

    @Override
    public void UpdateAdmin(String id, AdminUpdateRequest dto, MultipartFile avatar, MultipartFile cover) throws BadRequestException, IOException {
        // Create a new AppAdmin instance to update
        AppAdmin data = HandlerRepository.getEntityBySecureId(id, appAdminRepository, "Admin not found");
        data.setName(dto.name());

        // Check if the email has changed and if it exists
        String newEmail = dto.email() != null ? dto.email().toLowerCase() : null;
        assert newEmail != null;
        if (!newEmail.equals(data.getEmail())) {
            if (emailExists(newEmail, appAdminRepository)) {
                return;
            }
            data.setEmail(newEmail);
        }

        data.setPassword(dto.password() != null ? passwordEncoder.encode(dto.password()) : data.getPassword());
        Role role = HandlerRepository.getEntityBySecureId(dto.roleId(), roleRepository, "Role not found");
        data.setRole(role);
        data.setIsActive(dto.status());

        // Save the updated admin data
        AppAdmin savedAdmin = repository.save(data);
    }

    @Override
    public void DeleteAdmin(String id) throws BadRequestException {
        AppAdmin data = HandlerRepository.getEntityBySecureId(id, repository, "Admin not found");
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(appAdminRepository);
        int adminId = Math.toIntExact(data.getId());
        if (adminId >= 1 && adminId <= 5) {
            throw new BadRequestException("Cannot delete admin IDs 1-5");
        }
        // if admin itself cannot be deleted
        if (data.getId().equals(adminLogin.getId())) {
            throw new BadRequestException("Cannot delete admin itself");
        }

        repository.deleteById(data.getId());
    }

    @Override
    public ResultPageResponseDTO<AdminDetailResponse> AdminIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String roleId, Boolean status) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword,
                roleId,
                status
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Long idRole = roleId != null ? getRoleId(roleId) : null;
        Page<AppAdmin> pageResult = repository.getAdminList(set.keyword(), set.pageable(), idRole, status);
        List<AdminDetailResponse> dtos = pageResult.stream().map((c) -> {
            AdminDetailResponse dto = new AdminDetailResponse();
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public AdminCmsDetailResponse InfoAdmin() {
        AppAdmin data = GlobalConverter.getAdminEntity(appAdminRepository);

        return null;
    }


    // ------

    public boolean emailExists(String email, AppAdminRepository repository) {
        return repository.existsByEmail(email);
    }

    // -- helper --
    private Long getRoleId(String roleId) {
        Role data = HandlerRepository.getIdBySecureId(
                roleId,
                roleRepository::findByIdAndSecureId,
                projection -> roleRepository.findById(projection.getId()),
                "Role not found for ID: " + roleId
        );
        return data.getId();
    }

}
