package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.ContentModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface ContentService{

    ContentModelDTO.ContentDetailResponse findDataById(Long id) throws BadRequestException;

    List<ContentModelDTO.ContentDetailResponse> findAllData();

    void saveData(@Valid ContentModelDTO.ContentCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid ContentModelDTO.ContentUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}