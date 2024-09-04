package com.bca.byc.service;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BusinessCategoryCreateRequest;
import com.bca.byc.model.BusinessCategoryDetailResponse;
import com.bca.byc.model.BusinessCategoryUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface MsBusinessCategoryService {

    BusinessCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<BusinessCategoryDetailResponse> findByParentIdIsNull();
    List<BusinessCategoryDetailResponse> findByParentIdIsNotNull();
    List<BusinessCategoryDetailResponse> findAllData();

    List<BusinessCategoryDetailResponse> findByParent(BusinessCategory parentId);

    void saveData(@Valid BusinessCategoryCreateRequest dto) throws BadRequestException;
    void saveDataChild(Long id, @Valid BusinessCategoryCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid BusinessCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
