package com.bca.byc.service.impl;

import com.bca.byc.converter.FaqCategoryDTOConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.FaqCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCategoryCreateRequest;
import com.bca.byc.model.FaqCategoryDetailResponse;
import com.bca.byc.model.FaqCategoryUpdateRequest;
import com.bca.byc.repository.FaqCategoryRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.FaqCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {

    private final AppAdminRepository adminRepository;

    private final FaqCategoryRepository repository;
    private final FaqCategoryDTOConverter converter;

    private final String notFoundMessage = " not found";

    @Override
    public FaqCategoryDetailResponse findDataById(Long id) throws BadRequestException {
        FaqCategory data = HandlerRepository.getEntityById(id, repository, "Faq Category" + notFoundMessage);

        return converter.convertToListResponse(data);
    }

    @Override
    public List<FaqCategoryDetailResponse> findAllData() {
        // Get the list
        List<FaqCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid FaqCategoryCreateRequest dto) throws BadRequestException {
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "user" + notFoundMessage);
        // set entity to add with model mapper
        FaqCategory data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, FaqCategoryUpdateRequest dto) throws BadRequestException {
        // check exist and get
        FaqCategory data = HandlerRepository.getEntityById(id, repository, "Faq Category" + notFoundMessage);

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(Long id) throws BadRequestException {
        // delete data
        if (!repository.existsById(id)) {
            throw new BadRequestException("Faq Category" + notFoundMessage);
        } else {
            repository.deleteById(id);
        }
    }
}
