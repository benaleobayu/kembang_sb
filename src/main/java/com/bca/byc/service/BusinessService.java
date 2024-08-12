package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.api.BusinessModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface BusinessService {

    BusinessModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<BusinessModelDTO.DetailResponse> findAllData();

    void saveData(@Valid BusinessModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid BusinessModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
