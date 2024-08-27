package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCategoryModelDTO;

import jakarta.validation.Valid;
import java.util.List;

public interface FaqCategoryService {

    FaqCategoryModelDTO.FaqCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<FaqCategoryModelDTO.FaqCategoryDetailResponse> findAllData();

    void saveData(@Valid FaqCategoryModelDTO.FaqCategoryCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid FaqCategoryModelDTO.FaqCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
