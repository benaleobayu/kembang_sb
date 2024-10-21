package com.bca.byc.service.cms;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface RequestContactService {

    // --- public ---
    List<RequestContactDetailResponse> findAllData();
    // --- public ---

    ResultPageResponseDTO<RequestContactIndexResponse> listDataRequestContact(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    RequestContactDetailResponse findDataById(String id) throws BadRequestException;

    void updateData(String id, @Valid RequestContactUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}
