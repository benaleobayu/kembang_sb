package com.bca.byc.service.impl;

import com.bca.byc.converter.PreRegisterDTOConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.reponse.excel.PreRegisterExportResponse;
import com.bca.byc.repository.PreRegisterLogRepository;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.response.RejectRequest;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PreRegisterServiceImpl implements PreRegisterService {

    private PreRegisterRepository repository;
    private PreRegisterLogRepository logRepository;
    private PreRegisterDTOConverter converter;

    private AppAdminService adminService;

    @Override
    public PreRegisterDetailResponse findDataById(Long id) throws BadRequestException {
        PreRegister data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("PreRegister not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public ResultPageResponseDTO<PreRegisterDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword, LocalDate startDate, LocalDate endDate) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        // set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        Page<PreRegister> pageResult = repository.searchByKeywordAndDateRange(keyword, start, end, pageable);

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
    public void approveData(Long id, String email) throws BadRequestException {
        AppAdmin admin = adminService.findByEmail(email);
        if (admin == null) {
            throw new BadRequestException("Invalid email admin");
        }

        PreRegister data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("data pre register not found"));
        // update on converter
        converter.convertToApprovalRequest(data, admin);
        // save
        repository.save(data);
    }

    @Override
    public void rejectData(Long id, RejectRequest reason, String email) throws BadRequestException {
        AppAdmin admin = adminService.findByEmail(email);
        if (admin == null) {
            throw new BadRequestException("Invalid email admin");
        }

        PreRegister data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("data pre register not found"));
        // update on converter
        converter.convertToRejectRequest(data, reason, admin);
        // save
        repository.save(data);
    }

    @Override
    public List<PreRegisterExportResponse> findAllDataToExport() {
        return repository.findAll().stream()
                .map(converter::convertToExportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, List<?>>> listStatus() {
        List<String> statuses = Arrays.stream(AdminApprovalStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        List<AttributeResponse> listStatusResponse = statuses.stream()
                .map((c) -> {
                    AttributeResponse response = new AttributeResponse();
                    response.setId(statuses.indexOf(c));
                    response.setName(c);
                    return response;
                })
                .collect(Collectors.toList());

        List<Map<String, List<?>>> attributes = new ArrayList<>();

        Map<String, List<?>> listStatus = new HashMap<>();
        listStatus.put("status", listStatusResponse);
        attributes.add(listStatus);

        return attributes;
    }
}

