package com.bca.byc.service.impl;

import com.bca.byc.converter.AdminDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.repository.AdminRepository;
import com.bca.byc.response.AdminPermissionResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AdminService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private AdminRepository repository;
    private AdminDTOConverter converter;

    @Override
    public AppAdmin findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new BadRequestException("Admin not found")
        );
    }

    @Override
    public AdminDetailResponse findDataById(Long id) throws BadRequestException {
        AppAdmin data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Admin not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public AdminPermissionResponse getPermissionDetail(String email) {
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
    public void saveData(@Valid AdminCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        AppAdmin data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, AdminUpdateRequest dto) throws BadRequestException {
        // check exist and get
        AppAdmin data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Admin ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(Long id) throws BadRequestException {
        // delete data
        if (!repository.existsById(id)) {
            throw new BadRequestException("Admin not found");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public ResultPageResponseDTO<AdminDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : (keyword + "%");
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<AppAdmin> pageResult = repository.findByNameLikeIgnoreCase(keyword, pageable);
        List<AdminDetailResponse> dtos = pageResult.stream().map((c) -> {
            AdminDetailResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public AdminCmsDetailResponse getAdminDetail(String email) {
        AppAdmin data = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Admin not found"));

        return converter.convertToInfoResponse(data);
    }
}
