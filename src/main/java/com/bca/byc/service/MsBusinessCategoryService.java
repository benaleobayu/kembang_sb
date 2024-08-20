package com.bca.byc.service;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.BusinessCategoryModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface MsBusinessCategoryService {

    BusinessCategoryModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<BusinessCategoryModelDTO.DetailResponse> findByParentIdIsNull();
    List<BusinessCategoryModelDTO.DetailResponse> findByParentIdIsNotNull();
    List<BusinessCategoryModelDTO.DetailResponse> findAllData();

    List<BusinessCategoryModelDTO.DetailResponse> findByParent(BusinessCategory parentId);

    void saveData(@Valid BusinessCategoryModelDTO.CreateRequest dto) throws BadRequestException;
    void saveDataChild(Long id, @Valid BusinessCategoryModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid BusinessCategoryModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
