package com.bca.byc.service.impl;

import com.bca.byc.converter.ExpectCategoryDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectCategoryCreateUpdateRequest;
import com.bca.byc.model.ExpectCategoryIndexResponse;
import com.bca.byc.model.PublicExpectCategoryDetailResponse;
import com.bca.byc.repository.ExpectCategoryRepository;
import com.bca.byc.repository.ExpectItemRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.ExpectCategoryService;
import com.bca.byc.util.PaginationUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpectCategoryServiceImpl implements ExpectCategoryService {

    private final AppAdminRepository adminRepository;
    private final ExpectItemRepository expectItemRepository;
    private ExpectCategoryRepository repository;
    private ExpectCategoryDTOConverter converter;

    @Override
    public ResultPageResponseDTO<ExpectCategoryIndexResponse> listDataExpectCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<ExpectCategory> pageResult = repository.findByNameLikeIgnoreCase(keyword, pageable);
        List<ExpectCategoryIndexResponse> dtos = pageResult.stream().map((c) -> {
            ExpectCategoryIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ExpectCategoryIndexResponse findDataBySecureId(String id) throws BadRequestException {
        ExpectCategory data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");

        return converter.convertToDetailResponse(data);
    }

    @Override
    public List<PublicExpectCategoryDetailResponse> findAllData() {
        // Get the list
        List<ExpectCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveData(@Valid ExpectCategoryCreateUpdateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "Admin not found");
        // set entity to add with model mapper
        ExpectCategory data = converter.convertToCreateRequest(dto);
        // save data
        data.setCreatedBy(admin);
        ExpectCategory savedData = repository.save(data);
        if (dto.getSubCategories() != null) {
            for (String name : dto.getSubCategories()) {
                ExpectItem child = new ExpectItem();
                child.setName(name);
                child.setExpectCategory(savedData);
                expectItemRepository.save(child);
            }
        }
    }

    @Override
    @Transactional
    public void updateData(String id, @Valid ExpectCategoryCreateUpdateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "Admin not found");

        // Check existence and get the business category
        ExpectCategory data = HandlerRepository.getEntityBySecureId(id, repository, "Business category not found");

        // Update the main entity
        converter.convertToUpdateRequest(data, dto);
        data.setUpdatedAt(LocalDateTime.now());
        data.setUpdatedBy(admin);

        // Save the updated entity
        ExpectCategory savedData = repository.save(data);

        // Handle subcategories
        List<String> updatedSubCategoryNames = dto.getSubCategories() != null ? dto.getSubCategories() : new ArrayList<>();
        // Retrieve all existing subcategories for the current parent
        List<ExpectItem> existingChildren = expectItemRepository.findByParentId(savedData);

        // Remove subcategories that are no longer referenced
        for (ExpectItem child : existingChildren) {
            if (!updatedSubCategoryNames.contains(child.getName())) {
                // Mark as deleted or remove
                child.setIsDeleted(true);  // Assuming you have a soft delete mechanism
                expectItemRepository.save(child);
            }
        }

        // Handle addition or updating of subcategories
        for (String name : updatedSubCategoryNames) {
            // Find existing subcategory
            Optional<ExpectItem> existingChildOpt = expectItemRepository.findByNameSubCategory(savedData.getId(), name);

            ExpectItem child;
            if (existingChildOpt.isPresent()) {
                // If it exists, just update the parent
                child = existingChildOpt.get();
                child.setExpectCategory(savedData);  // Update to the new parent
            } else {
                // If it doesn't exist, create a new one
                child = new ExpectItem();
                child.setName(name);
                child.setExpectCategory(savedData);
            }
            // Save the child (either existing or new)
            expectItemRepository.save(child);
        }
    }

    @Override
    @Transactional
    public void deleteData(String id) throws BadRequestException {
        ExpectCategory data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");
        Long expectId = data.getId();
        // delete data
        if (!repository.existsById(expectId)) {
            throw new BadRequestException("ExpectCategory not found");
        }
        if (!data.getUserHasExpects().isEmpty()) {
            throw new BadRequestException("Cannot delete Expect category because it is used in user");
        }
        repository.deleteById(expectId);
    }
}
