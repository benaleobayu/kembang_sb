package com.bca.byc.service;

import com.bca.byc.model.AccountCreateUpdateRequest;
import com.bca.byc.model.AccountDetailResponse;
import com.bca.byc.model.AccountIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface AccountService {

    ResultPageResponseDTO<AccountIndexResponse> listDataAccountIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    AccountDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(@Valid AccountCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid AccountCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}
