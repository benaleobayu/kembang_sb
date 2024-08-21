package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.FaqCategoryModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface FaqCategoryService {

    FaqCategoryModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<FaqCategoryModelDTO.DetailResponse> findAllData();

    void saveData(@Valid FaqCategoryModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid FaqCategoryModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
