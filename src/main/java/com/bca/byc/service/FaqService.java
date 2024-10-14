package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCreateUpdateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

public interface FaqService {

    ResultPageResponseDTO<FaqIndexResponse> FaqItemIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String categoryId);

    FaqDetailResponse DetailFaqItem(String categoryId, String itemId);

    void CreateFaqItem(String categoryId, @Valid FaqCreateUpdateRequest dto) throws BadRequestException;

    void UpdateFaqItem(String categoryId, String itemId, @Valid FaqCreateUpdateRequest dto) throws BadRequestException;

    void DeleteFaqItem(String categoryId, String itemId) throws BadRequestException;

}
