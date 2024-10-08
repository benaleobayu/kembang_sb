package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCreateUpdateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

public interface FaqService {

    ResultPageResponseDTO<FaqIndexResponse> listDataFaq(Integer pages, Integer limit, String sortBy, String direction, String keyword, String categoryId);

    FaqDetailResponse findDataById(String categoryId, String itemId);

    void saveData(String categoryId, @Valid FaqCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String categoryId, String itemId, @Valid FaqCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String categoryId, String itemId) throws BadRequestException;

}
