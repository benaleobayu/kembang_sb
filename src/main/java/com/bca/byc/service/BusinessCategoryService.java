package com.bca.byc.service;

import com.bca.byc.model.cms.BusinessCategoryCreateRequest;
import com.bca.byc.model.cms.BusinessCategoryDetailResponse;
import com.bca.byc.model.cms.BusinessCategoryUpdateRequest;

import javax.validation.Valid;
import java.util.List;

public interface BusinessCategoryService {

//    boolean existsById(Long id);

    BusinessCategoryDetailResponse findDataById(Long id);

    List<BusinessCategoryDetailResponse> findAllData();

    void saveData(@Valid BusinessCategoryCreateRequest dto) throws Exception;

    void updateData(Long id, @Valid BusinessCategoryUpdateRequest dto);

    void deleteData(Long id);
}
