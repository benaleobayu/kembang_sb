package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.FaqCategoryDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.FaqCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCategoryCreateUpdateRequest;
import com.bca.byc.model.FaqCategoryDetailResponse;
import com.bca.byc.model.FaqCategoryIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.FaqCategoryRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.FaqCategoryService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {

    private final AppAdminRepository adminRepository;

    private final FaqCategoryRepository repository;
    private final FaqCategoryDTOConverter converter;

    private final String notFoundMessage = "Faq Category not found";

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
    public FaqCategoryDetailResponse findDataById(String id) throws BadRequestException {
        FaqCategory data = HandlerRepository.getEntityBySecureId(id, repository, notFoundMessage);

        return converter.convertToListResponse(data);
    }

    @Override
    public ResultPageResponseDTO<FaqCategoryIndexResponse> listDataFaqCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<FaqCategory> pageResult = repository.findByNameLikeIgnoreCase(set.keyword(), set.pageable());
        List<FaqCategoryIndexResponse> dtos = pageResult.stream().map((c) -> {
            FaqCategoryIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }


    @Override
    public void saveData(@Valid FaqCategoryCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        // set entity to add with model mapper
        FaqCategory data = converter.convertToCreateRequest(dto);
        data.setCreatedBy(admin);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, @Valid FaqCategoryCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        FaqCategory data = HandlerRepository.getEntityBySecureId(id, repository, notFoundMessage);
        // update
        data.setName(dto.getName());
        data.setDescription(dto.getDescription());
        data.setIsActive(dto.getStatus());
        data.setOrders(dto.getOrders());

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        FaqCategory data = HandlerRepository.getEntityBySecureId(id, repository, notFoundMessage);
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException(notFoundMessage);
        } else {
            repository.deleteById(data.getId());
        }
    }
}
