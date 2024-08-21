package com.bca.byc.service;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.BusinessCategoryModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface MsBusinessCategoryService {

    BusinessCategoryModelDTO.BusinessCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> findByParentIdIsNull();
    List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> findByParentIdIsNotNull();
    List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> findAllData();

    List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> findByParent(BusinessCategory parentId);

    void saveData(@Valid BusinessCategoryModelDTO.BusinessCategoryCreateRequest dto) throws BadRequestException;
    void saveDataChild(Long id, @Valid BusinessCategoryModelDTO.BusinessCategoryCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid BusinessCategoryModelDTO.BusinessCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
