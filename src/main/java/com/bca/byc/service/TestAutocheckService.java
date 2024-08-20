package com.bca.byc.service;

import javax.validation.Valid;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.test.TestAutocheckModelDTO;

import java.util.List;

public interface TestAutocheckService {

    TestAutocheckModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<TestAutocheckModelDTO.DetailResponse> findAllData();

    void saveData(@Valid TestAutocheckModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid TestAutocheckModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
