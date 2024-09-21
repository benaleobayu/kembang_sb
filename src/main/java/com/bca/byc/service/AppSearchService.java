package com.bca.byc.service;

import com.bca.byc.model.AppSearchDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface AppSearchService {

    ResultPageResponseDTO<AppSearchDetailResponse> listResultPosts(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AppSearchDetailResponse> listResultTags(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AppSearchDetailResponse> listResultAccounts(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword);
}

