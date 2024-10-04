package com.bca.byc.service.impl;

import com.bca.byc.converter.InputAttributeDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.Branch;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.AttributeNameResponse;
import com.bca.byc.entity.impl.AttrIdentificable;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.repository.BranchRepository;
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
    private final BranchRepository branchRepository;

    private final InputAttributeDTOConverter converter;

    @Override
    public ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryBusiness(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<BusinessCategory> pageResult = businessCategoryRepository.findIdAndName(keyword, pageable);
        List<AttributeNameResponse> dtos = pageResult.stream()
                .map(this::convertToListAttributeOnName)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryExpect(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<ExpectItem> pageResult = expectItemRepository.findIdAndName(keyword, pageable);
        List<AttributeNameResponse> dtos = pageResult.stream()
                .map(this::convertToListAttributeOnName)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AttributeResponse> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Branch> pageResult = branchRepository.findIdAndName(keyword, pageable);
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    public AttributeNameResponse convertToListAttributeOnName(AttrIdentificable dto) {
        AttributeNameResponse response = new AttributeNameResponse();
        response.setName(dto.getName());
        return response;
    }

    public AttributeResponse convertToListAttribute(AttrIdentificable dto) {
        AttributeResponse<String> response = new AttributeResponse<>();
        response.setId(dto.getSecureId());
        response.setName(dto.getName());
        return response;
    }




}
