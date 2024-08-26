package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.test.AutocheckCreateRequest;
import com.bca.byc.model.test.AutocheckDetailResponse;
import com.bca.byc.model.test.AutocheckUpdateRequest;

import javax.validation.Valid;
import java.util.List;

public interface TestAutocheckService {

    AutocheckDetailResponse findDataById(Long id) throws BadRequestException;

    List<AutocheckDetailResponse> findAllData();

    void saveData(@Valid AutocheckCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid AutocheckUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
