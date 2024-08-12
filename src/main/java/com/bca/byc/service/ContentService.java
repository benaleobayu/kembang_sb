package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.ContentModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface ContentService{

    ContentModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<ContentModelDTO.DetailResponse> findAllData();

    void saveData(@Valid ContentModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid ContentModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}