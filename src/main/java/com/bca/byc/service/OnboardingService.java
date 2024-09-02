package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.OnboardingListUserResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.model.OnboardingModelDTO;

public interface OnboardingService {

    void createData(String email,@Valid OnboardingModelDTO.OnboardingCreateRequest dto) throws BadRequestException;

    ResultPageResponseDTO<OnboardingListUserResponse> listFollowUser(Integer pages, Integer limit, String sortBy, String direction, String userName);


}
