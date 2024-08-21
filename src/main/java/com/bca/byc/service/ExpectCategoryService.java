package com.bca.byc.service;

import javax.validation.Valid;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectCategoryModelDTO;

import java.util.List;

public interface ExpectCategoryService {

    ExpectCategoryModelDTO.ExpectCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<ExpectCategoryModelDTO.ExpectCategoryDetailResponse> findAllData();

    void saveData(@Valid ExpectCategoryModelDTO.ExpectCategoryCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid ExpectCategoryModelDTO.ExpectCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
