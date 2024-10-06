package com.bca.byc.service;

import com.bca.byc.model.BlacklistIndexResponse;
import com.bca.byc.model.BlacklistKeywordCreateUpdateRequest;
import com.bca.byc.model.BlacklistKeywordDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.io.IOException;

public interface BlacklistKeywordService {

    ResultPageResponseDTO<BlacklistIndexResponse> listDataBlacklist(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    BlacklistKeywordDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(@Valid BlacklistKeywordCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid BlacklistKeywordCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;

    void exportExcel(HttpServletResponse response) throws IOException;
}
