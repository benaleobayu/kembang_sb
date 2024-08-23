package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import jakarta.validation.Valid;
import com.bca.byc.model.OnboardingModelDTO;

public interface OnboardingService {

    void createData(String email,@Valid OnboardingModelDTO.OnboardingCreateRequest dto) throws BadRequestException;

}
