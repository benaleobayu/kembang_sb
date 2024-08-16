package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.FeedbackCategoryModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface MsFeedbackCategoryService {

    FeedbackCategoryModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<FeedbackCategoryModelDTO.DetailResponse> findAllData();

    void saveData(@Valid FeedbackCategoryModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid FeedbackCategoryModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
