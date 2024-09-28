package com.bca.byc.service.cms;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BranchCreateUpdateRequest;
import com.bca.byc.model.BranchDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

public interface BranchService {

    ResultPageResponseDTO<BranchDetailResponse> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    BranchDetailResponse findDataBySecureId(String id) throws BadRequestException;

    void saveData(@Valid BranchCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid BranchCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}

