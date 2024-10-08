package com.bca.byc.service.impl;

import com.bca.byc.converter.BusinessCategoryDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.BusinessCategoryIndexResponse;
import com.bca.byc.model.BusinessCategoryListResponse;
import com.bca.byc.model.BusinessCategoryParentCreateRequest;
import com.bca.byc.model.BusinessCategoryUpdateRequest;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.BusinessCategoryService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
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
public class BusinessCategoryServiceImpl implements BusinessCategoryService {

    private final AppAdminRepository adminRepository;

    private final BusinessRepository businessRepository;
    private BusinessCategoryRepository repository;
    private BusinessCategoryDTOConverter converter;


    @Override
    public List<BusinessCategoryListResponse> findByParentIdIsNotNull() {
        // Get the list
        List<BusinessCategory> datas = repository.findByParentIdIsNotNull();
        for (BusinessCategory item : datas) {
            if (item.getDescription() != null) {
                String cleanDescription = Jsoup.parse(item.getDescription()).text();
                item.setDescription(cleanDescription);
            }
        }

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessCategoryListResponse> findByParentIdIsNull() {
        // Get the list
        List<BusinessCategory> datas = repository.findByParentIdIsNull();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResultPageResponseDTO<BusinessCategoryIndexResponse> listDataBusinessCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<BusinessCategory> pageResult = repository.findDataByKeyword(keyword, pageable);
        List<BusinessCategoryIndexResponse> dtos = pageResult.stream().map((c) -> {
            BusinessCategoryIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public BusinessCategoryIndexResponse findDataBySecureId(String id) {
        BusinessCategory data = repository.findBySecureId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business Category not found"));
        return converter.convertToIndexResponse(data);
    }

    @Override
    public void saveData(@Valid BusinessCategoryParentCreateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "Admin not found");
        // set entity to add with model mapper
        BusinessCategory data = converter.convertToCreateParentRequest(dto);
        data.setCreatedBy(admin);
        BusinessCategory savedData = repository.save(data);

        // add children from list<String> e.g ["id1", "id2", "id3"] to add to new BusinessCategory with assign the parentId
        if (dto.getSubCategories() != null) {
            for (String name : dto.getSubCategories()) {
                BusinessCategory child = new BusinessCategory();
                child.setName(name);
                child.setIsParent(false);
                child.setParentId(savedData);
                repository.save(child);
            }
        }
    }

    @Override
    public void updateData(String id, @Valid BusinessCategoryUpdateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "Admin not found");

        // Check existence and get the business category
        BusinessCategory data = HandlerRepository.getEntityBySecureId(id, repository, "Business category not found");

        // Update the main entity
        converter.convertToUpdateRequest(data, dto);
        data.setUpdatedAt(LocalDateTime.now());
        data.setUpdatedBy(admin);

        // Save the updated entity
        BusinessCategory savedData = repository.save(data);

        // Handle subcategories
        List<String> updatedSubCategoryNames = dto.getSubCategories() != null ? dto.getSubCategories() : new ArrayList<>();
        // Retrieve all existing subcategories for the current parent
        List<BusinessCategory> existingChildren = repository.findByParentId(savedData);

        // Remove subcategories that are no longer referenced
        for (BusinessCategory child : existingChildren) {
            if (!updatedSubCategoryNames.contains(child.getName())) {
                // Mark as deleted or remove
                child.setIsDeleted(true);  // Assuming you have a soft delete mechanism
                repository.save(child);
            }
        }

        // Handle addition or updating of subcategories
        for (String name : updatedSubCategoryNames) {
            // Find existing subcategory
            Optional<BusinessCategory> existingChildOpt = repository.findByNameSubCategory(savedData.getId(), name);

            BusinessCategory child;
            if (existingChildOpt.isPresent()) {
                // If it exists, just update the parent
                child = existingChildOpt.get();
                child.setParentId(savedData);  // Update to the new parent
            } else {
                // If it doesn't exist, create a new one
                child = new BusinessCategory();
                child.setName(name);
                child.setParentId(savedData);
            }
            // Save the child (either existing or new)
            repository.save(child);
        }
    }




    @Override
    public void deleteData(String id) throws BadRequestException {
        BusinessCategory data = HandlerRepository.getEntityBySecureId(
                id, repository, "Business category not found"
        );

        // Check if the BusinessCategory exists
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("Business category not found");
        }

        // Check if any Business is using this BusinessCategory
        boolean isUsed = businessRepository.existsByBusinessCategories(data);
        if (isUsed) {
            throw new BadRequestException("Cannot delete category because it is used in a business");
        }

        // Delete the BusinessCategory
        repository.deleteById(data.getId());
    }

    @Override
    public BusinessCategory getCategoryById(Long id) {
        Optional<BusinessCategory> categoryOptional = repository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        } else {
            throw new ResourceNotFoundException("Category with ID " + id + " not found");
        }
    }
    

}
