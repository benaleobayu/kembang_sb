package com.bca.byc.service;

import javax.validation.Valid;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.FaqModelDTO;

import java.util.List;

public interface FaqService {

    FaqModelDTO.FaqDetailResponse findDataById(Long id) throws BadRequestException;

    List<FaqModelDTO.FaqDetailResponse> findAllData();

    void saveData(@Valid FaqModelDTO.FaqCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid FaqModelDTO.FaqUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
