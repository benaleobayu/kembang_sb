package com.bca.byc.service.cms;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.KanwilCreateUpdateRequest;
import com.bca.byc.model.KanwilDetailResponse;
import com.bca.byc.model.KanwilListResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

public interface KanwilService {


    ResultPageResponseDTO<KanwilListResponse> listDataKanwil(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    KanwilDetailResponse findDataBySecureId(String id) throws BadRequestException;

    void saveData(@Valid KanwilCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid KanwilCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}

