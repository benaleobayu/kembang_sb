package com.bca.byc.service.cms;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface LogRequestService {

    // --- public ---
    List<LogRequestDetailResponse> findAllData();
    // --- public ---

    ResultPageResponseDTO<LogRequestIndexResponse> listLogRequestByModelableId(String id, Integer pages, Integer limit, String sortBy, String direction, String keyword);

    LogRequestDetailResponse findDataById(String id) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}
