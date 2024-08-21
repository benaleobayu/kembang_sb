package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FeedbackCategoryModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface MsFeedbackCategoryService {

    FeedbackCategoryModelDTO.FeedbackCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<FeedbackCategoryModelDTO.FeedbackCategoryDetailResponse> findAllData();

    void saveData(@Valid FeedbackCategoryModelDTO.FeedbackCategoryCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid FeedbackCategoryModelDTO.FeedbackCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
