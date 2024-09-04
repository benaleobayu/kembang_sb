package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.OnboardingCreateRequest;
import com.bca.byc.model.OnboardingListUserResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

public interface OnboardingService {
    void createData(String email,@Valid OnboardingCreateRequest dto) throws BadRequestException;

    ResultPageResponseDTO<OnboardingListUserResponse> listFollowUser(Integer pages, Integer limit, String sortBy, String direction, String userName);

}
