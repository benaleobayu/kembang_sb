package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.api.InterestModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface InterestService {

    InterestModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<InterestModelDTO.DetailResponse> findAllData();

    void saveData(@Valid InterestModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid InterestModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
