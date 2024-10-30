package com.bca.byc.service;

import com.bca.byc.model.AppSearchDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.SuggestedUserResponse;
import com.bca.byc.model.search.SearchDTOResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface AppSearchService {

    ResultPageResponseDTO<PostHomeResponse> listResultPosts(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AppSearchDetailResponse> listResultTags(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<SuggestedUserResponse> listResultAccounts(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<SuggestedUserResponse> listSuggestedUser(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<SearchDTOResponse> listSearch(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}

