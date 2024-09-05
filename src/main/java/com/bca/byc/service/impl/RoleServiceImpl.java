package com.bca.byc.service.impl;

import com.bca.byc.converter.RoleDTOConverter;
import com.bca.byc.entity.Role;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.model.RoleCreateRequest;
import com.bca.byc.service.RoleService;
import com.bca.byc.model.RoleUpdateRequest;
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
public class RoleServiceImpl implements RoleService {

    private RoleRepository repository;
    private RoleDTOConverter converter;

    @Override
    public RoleDetailResponse findDataById(Long id) throws BadRequestException {
        Role data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Role not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<RoleDetailResponse> findAllData() {
        // Get the list
        List<Role> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid RoleCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Role data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, RoleUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Role data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Role ID"));

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
            throw new BadRequestException("Role not found");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public ResultPageResponseDTO<RoleDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : (userName + "%");
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Role> pageResult = repository.findByNameLikeIgnoreCase(userName, pageable);
        List<RoleDetailResponse> dtos = pageResult.stream().map((c) -> {
            RoleDetailResponse dto = converter.convertToListResponse(c);
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
