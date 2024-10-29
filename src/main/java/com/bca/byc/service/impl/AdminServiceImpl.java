package com.bca.byc.service.impl;

import com.bca.byc.converter.AdminDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.AccountRepository;
import com.bca.byc.repository.AdminHasAccountRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bca.byc.util.FileUploadHelper.saveFile;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AppAdminRepository appAdminRepository;
    private final AdminRepository repository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final AdminHasAccountRepository adminHasAccountRepository;

    private final PasswordEncoder passwordEncoder;

    private final AdminDTOConverter converter;

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

        return converter.convertToListResponse(data);
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
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
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
        data.setIsVisible(dto.isVisible());

        String avatarUrl = FileUploadHelper.saveFile(avatar, UPLOAD_DIR + "/admin/avatar");
        data.setAvatar(GlobalConverter.getParseImage(avatarUrl, baseUrl));

        String coverUrl = FileUploadHelper.saveFile(cover, UPLOAD_DIR + "/admin/cover");
        data.setCover(GlobalConverter.getParseImage(coverUrl, baseUrl));


        // save data
        AppAdmin savedAdmin = repository.save(data);

        // list of account ids e.g ['abc-def', 'ghi-jkl']
        saveAccountInAdmin(savedAdmin, dto.accountIds());
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
        data.setIsVisible(dto.isVisible());
        if (avatar != null) {
            FileUploadHelper.validateFileTypeImage(avatar);
            String oldAvatar = data.getAvatar();
            String newAvatar = saveFile(avatar, UPLOAD_DIR + "/admin/avatar");
            data.setAvatar(GlobalConverter.replaceImagePath(newAvatar));
            if (!oldAvatar.equals(newAvatar)) {
                FileUploadHelper.deleteFile(oldAvatar, UPLOAD_DIR);
            }
        }

        if (cover != null) {
            FileUploadHelper.validateFileTypeImage(cover);
            String oldCover = data.getCover();
            String newCover = saveFile(cover, UPLOAD_DIR + "/admin/cover");
            data.setCover(GlobalConverter.replaceImagePath(newCover));
            if (!oldCover.equals(newCover)) {
                FileUploadHelper.deleteFile(oldCover, UPLOAD_DIR);
            }
        }
        // Save the updated admin data
        AppAdmin savedAdmin = repository.save(data);

        // remove first of all data
        adminHasAccountRepository.deleteByAdminId(data.getId());
        // list of account ids e.g ['abc-def', 'ghi-jkl']
        saveAccountInAdmin(savedAdmin, dto.accountIds());
    }

    @Override
    public void UpdateProfileAdmin(UpdateProfileAdminRequest dto) {
        AppAdmin data = GlobalConverter.getAdminEntity(appAdminRepository);

        data.setName(dto.getName());
        data.setEmail(dto.getEmail());
        if (dto.getOldPassword() != null && dto.getNewPassword() != null && dto.getConfirmPassword() != null) {
            if (!passwordEncoder.matches(dto.getOldPassword(), data.getPassword())) {
                throw new BadRequestException("Old password is incorrect");
            }
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                throw new BadRequestException("Confirm password is incorrect");
            }

            data.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }
        data.setIsActive(dto.getStatus());
        data.setIsVisible(dto.getIsVisible());
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
            AdminDetailResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public AdminCmsDetailResponse InfoAdmin(String email) {
        AppAdmin data = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Admin not found"));

        return converter.convertToInfoResponse(data);
    }


    // ------

    public boolean emailExists(String email, AppAdminRepository repository) {
        return repository.existsByEmail(email);
    }

    private void saveAccountInAdmin(AppAdmin savedAdmin, Set<String> accountIds) {
        if (accountIds != null) {
            Set<Account> accounts = new HashSet<>();
            for (String accountId : accountIds) {
                Account account = HandlerRepository.getIdBySecureId(
                        accountId,
                        accountRepository::findByIdAndSecureId,
                        projection -> accountRepository.findById(projection.getId()),
                        "Account not found for ID: " + accountId
                );
                accounts.add(account);
            }
            for (Account account : accounts) {
                AdminHasAccounts adminHasAccount = new AdminHasAccounts();
                adminHasAccount.setAdmin(savedAdmin);
                adminHasAccount.setAccount(account);
                adminHasAccountRepository.save(adminHasAccount);
            }
        }
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
