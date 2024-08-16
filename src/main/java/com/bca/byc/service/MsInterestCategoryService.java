package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.InterestCategoryModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface MsInterestCategoryService {

    InterestCategoryModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<InterestCategoryModelDTO.DetailResponse> findAllData();

    void saveData(@Valid InterestCategoryModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid InterestCategoryModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
