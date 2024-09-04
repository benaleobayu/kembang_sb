package com.bca.byc.service.impl;

import com.bca.byc.converter.AdminDTOConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminCmsDetailResponse;
import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import com.bca.byc.repository.AdminRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private AdminRepository repository;
    private AdminDTOConverter converter;

    @Override
    public AdminDetailResponse findDataById(Long id) throws BadRequestException {
        AppAdmin data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Admin not found"));

        return converter.convertToListResponse(data);
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
    public ResultPageResponseDTO<AdminDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<AppAdmin> pageResult = repository.findByNameLikeIgnoreCase(userName, pageable);
        List<AdminDetailResponse> dtos = pageResult.stream().map((c) -> {
            AdminDetailResponse dto = converter.convertToListResponse(c);
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
    public AdminCmsDetailResponse getAdminDetail(String email) {
        AppAdmin user = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email not found"));

        AppAdmin data = repository.findById(user.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return converter.convertToInfoResponse(data);
    }
}
