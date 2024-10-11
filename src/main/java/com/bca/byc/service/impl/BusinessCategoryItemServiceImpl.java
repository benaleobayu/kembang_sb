package com.bca.byc.service.impl;

import com.bca.byc.converter.BusinessCategoryDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BusinessCategoryItemCreateRequest;
import com.bca.byc.model.BusinessCategoryItemListResponse;
import com.bca.byc.model.BusinessCategoryUpdateRequest;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.BusinessCategoryItemService;
import com.bca.byc.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessCategoryItemServiceImpl implements BusinessCategoryItemService {

    private BusinessCategoryRepository repository;
    private BusinessCategoryDTOConverter converter;

    @Override
    public ResultPageResponseDTO<BusinessCategoryItemListResponse> listDataBusinessCategoryItem(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<BusinessCategory> pageResult = repository.findDataItemByKeyword(set.keyword(), set.pageable());
        List<BusinessCategoryItemListResponse> dtos = pageResult.stream().map((c) -> {
            BusinessCategoryItemListResponse dto = converter.convertToListItemResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public BusinessCategoryItemListResponse findDataBySecureId(String parentId, String id) throws BadRequestException {
        BusinessCategory data = HandlerRepository.getEntityBySecureId(
                id, repository, "Business category item not found"
        );

        return converter.convertToListItemResponse(data);
    }

    @Override
    public void saveData(String parentid, BusinessCategoryItemCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        BusinessCategory data = converter.convertToCreateChildRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String parentId, String id, BusinessCategoryUpdateRequest dto) {
        // check exist and get
        BusinessCategory data = HandlerRepository.getEntityBySecureId(
                id, repository, "Business category item not found"
        );
        if (!data.getParentId().equals(parentId)) {
            throw new BadRequestException("Parent not match with data id");
        }
        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String parentId, String id) throws BadRequestException {
        BusinessCategory data = HandlerRepository.getEntityBySecureId(
                id, repository, "Business category item not found"
        );

        if (!data.getParentId().equals(parentId)) {
            throw new BadRequestException("Parent not match with data id");
        }
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("BusinessCategoryItem not found");
        } else {
            repository.deleteById(data.getId());
        }
    }
}