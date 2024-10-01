package com.bca.byc.service.impl;

import com.bca.byc.converter.InputAttributeDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.entity.SecureIdentifiable;
import com.bca.byc.model.attribute.AttrIdentificable;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.repository.ExpectItemRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.InputAttributeService;
import com.bca.byc.util.PaginationUtil;
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
public class InputAttributeServiceImpl implements InputAttributeService {

    private final BusinessCategoryRepository businessCategoryRepository;
    private final ExpectItemRepository expectItemRepository;

    private final InputAttributeDTOConverter converter;

    @Override
    public ResultPageResponseDTO<AttributeResponse> listDataSubCategoryBusiness(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<BusinessCategory> pageResult = businessCategoryRepository.findIdAndName(keyword, pageable);
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AttributeResponse> listDataSubCategoryExpect(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<ExpectItem> pageResult = expectItemRepository.findIdAndName(keyword, pageable);
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    public AttributeResponse convertToListAttribute(AttrIdentificable dto) {
        AttributeResponse<Long> response = new AttributeResponse<>();
        response.setId(dto.getId());
        response.setName(dto.getName());
        return response;
    }


}
