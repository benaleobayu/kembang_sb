package com.bca.byc.service.impl;

import com.bca.byc.converter.PreRegisterDTOConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppAdminService;
import com.bca.byc.service.PreRegisterService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PreRegisterServiceImpl implements PreRegisterService {

    private PreRegisterRepository repository;
    private PreRegisterDTOConverter converter;

    private AppAdminService adminService;

    @Override
    public PreRegisterDetailResponse findDataById(Long id) throws BadRequestException {
        PreRegister data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("PreRegister not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<PreRegisterDetailResponse> findAllData() {
        // Get the list
        List<PreRegister> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid PreRegisterCreateRequest dto, String email) throws BadRequestException {
        // check if email exists return error
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        AppAdmin admin = adminService.findByEmail(email);

        // set entity to add with model mapper
        PreRegister data = converter.convertToCreateRequest(dto, admin);

        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, PreRegisterUpdateRequest dto) throws BadRequestException {
        // check exist and get
        PreRegister data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID PreRegister ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(List<Long> ids) throws BadRequestException {
        List<PreRegister> users = repository.findAllById(ids);
        if (users.isEmpty()) {
            throw new BadRequestException("user on pre-register not found");
        }
        repository.deleteAll(users);
    }

    @Override
    public ResultPageResponseDTO<PreRegisterDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<PreRegister> pageResult = repository.findByNameLikeIgnoreCase(userName, pageable);
        List<PreRegisterDetailResponse> dtos = pageResult.stream().map((c) -> {
            PreRegisterDetailResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        int currentPage = pageResult.getNumber() + 1;
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage > 1 ? currentPage - 1 : 1, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                1, // first page
                totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }
}

