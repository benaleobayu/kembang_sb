package com.bca.byc.service;

import com.bca.byc.model.cms.InterestCategoryCreateRequest;
import com.bca.byc.model.cms.InterestCategoryDetailResponse;
import com.bca.byc.model.cms.InterestCategoryUpdateRequest;

import javax.validation.Valid;
import java.util.List;

public interface InterestCategoryService {

//    boolean existsById(Long id);

    InterestCategoryDetailResponse findDataById(Long id);

    List<InterestCategoryDetailResponse> findAllData();

    void saveData(@Valid InterestCategoryCreateRequest dto) throws Exception;

    void updateData(Long id, @Valid InterestCategoryUpdateRequest dto);

    void deleteData(Long id);
}
