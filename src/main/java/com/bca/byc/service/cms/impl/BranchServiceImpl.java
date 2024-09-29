package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.BranchDTOConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Branch;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BranchCreateUpdateRequest;
import com.bca.byc.model.BranchDetailResponse;
import com.bca.byc.repository.BranchRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.cms.BranchService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BranchServiceImpl implements BranchService {

    static String notFound = "not found";
    private final AppAdminRepository adminRepository;
    private final BranchRepository repository;
    private final BranchDTOConverter converter;

    @Override
    public ResultPageResponseDTO<BranchDetailResponse> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Branch> pageResult = repository.findDataByKeyword(keyword, pageable);
        List<BranchDetailResponse> dtos = pageResult.stream().map((c) -> {
            BranchDetailResponse dto = converter.convertToListResponse(c);
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
    public BranchDetailResponse findDataBySecureId(String id) throws BadRequestException {
        Branch data = HandlerRepository.getEntityBySecureId(id, repository, "Branch" + notFound);

        return converter.convertToListResponse(data);
    }

    @Override
    public void saveData(@Valid BranchCreateUpdateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "Admin" + notFound);
        // set entity to add with model mapper
        Branch data = converter.convertToCreateRequest(dto, admin);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, BranchCreateUpdateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "Admin" + notFound);
        Branch data = HandlerRepository.getEntityBySecureId(id, repository, "Branch" + notFound);

        // update
        converter.convertToUpdateRequest(data, dto, admin);

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        Branch user = HandlerRepository.getEntityBySecureId(id, repository, "Branch" + notFound);
        Long dataId = user.getId();
        // delete data
        if (!repository.existsById(dataId)) {
            throw new BadRequestException("Branch not found");
        } else {
            repository.deleteById(dataId);
        }
    }
}
