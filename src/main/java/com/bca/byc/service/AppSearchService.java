package com.bca.byc.service;

import com.bca.byc.model.AppSearchDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface AppSearchService {

    ResultPageResponseDTO<AppSearchDetailResponse> listData(String email, Integer pages, Integer limit, String sortBy, String direction, String tag, String categories);
}

