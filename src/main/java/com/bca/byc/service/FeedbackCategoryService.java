package com.bca.byc.service;

import com.bca.byc.model.cms.FeedbackCategoryCreateRequest;
import com.bca.byc.model.cms.FeedbackCategoryDetailResponse;
import com.bca.byc.model.cms.FeedbackCategoryUpdateRequest;

import javax.validation.Valid;
import java.util.List;

public interface FeedbackCategoryService {

//    boolean existsById(Long id);

    FeedbackCategoryDetailResponse findDataById(Long id);

    List<FeedbackCategoryDetailResponse> findAllData();

    void saveData(@Valid FeedbackCategoryCreateRequest dto) throws Exception;

    void updateData(Long id, @Valid FeedbackCategoryUpdateRequest dto);

    void deleteData(Long id);
}
