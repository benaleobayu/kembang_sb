package com.bca.byc.service;

import javax.validation.Valid;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.test.TestAutocheckModelDTO;

import java.util.List;

public interface TestAutocheckService {

    TestAutocheckModelDTO.TestAutocheckDetailResponse findDataById(Long id) throws BadRequestException;

    List<TestAutocheckModelDTO.TestAutocheckDetailResponse> findAllData();

    void saveData(@Valid TestAutocheckModelDTO.TestAutocheckCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid TestAutocheckModelDTO.TestAutocheckUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
