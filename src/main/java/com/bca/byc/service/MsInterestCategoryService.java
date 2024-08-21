package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.InterestCategoryModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface MsInterestCategoryService {

    InterestCategoryModelDTO.InterestCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<InterestCategoryModelDTO.InterestCategoryDetailResponse> findAllData();

    void saveData(@Valid InterestCategoryModelDTO.InterestCategoryCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid InterestCategoryModelDTO.InterestCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
