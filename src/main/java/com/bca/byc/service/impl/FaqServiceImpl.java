package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.Faq;
import com.bca.byc.entity.FaqCategory;
import com.bca.byc.model.FaqCreateUpdateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqIndexResponse;
import com.bca.byc.repository.FaqCategoryRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
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

import com.bca.byc.converter.FaqDTOConverter;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.repository.FaqRepository;
import com.bca.byc.service.FaqService;

@Service
@AllArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqRepository repository;
    private final FaqDTOConverter converter;

    @Override
    public ResultPageResponseDTO<FaqIndexResponse> listDataFaq(Integer pages, Integer limit, String sortBy, String direction, String keyword, String categoryId) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Faq> pageResult = repository.findByNameLikeIgnoreCase(keyword, pageable);
        List<FaqIndexResponse> dtos = pageResult.stream().map((c) -> {
            FaqIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public FaqDetailResponse findDataById(String categoryId, String itemId) throws BadRequestException {
        Faq item = HandlerRepository.getEntityBySecureId(itemId, repository, "Faq not found");
        FaqCategory category = HandlerRepository.getEntityBySecureId(categoryId, faqCategoryRepository, "Category not found");
        if (!item.getFaqCategoryId().equals(category.getId())) {
            throw new BadRequestException("Faq not belong to this category");
        }

        return converter.convertToDetailResponse(item);
    }

    @Override
    public void saveData(String categoryId, @Valid FaqCreateUpdateRequest dto) throws BadRequestException {
        FaqCategory category = HandlerRepository.getEntityBySecureId(categoryId, faqCategoryRepository, "Category not found");
        // set entity to add with model mapper
        Faq data = converter.convertToCreateRequest(dto);
        data.setFaqCategoryId(category);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String categoryId, String itemId, FaqCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Faq item = HandlerRepository.getEntityBySecureId(itemId, repository, "Faq not found");
        FaqCategory category = HandlerRepository.getEntityBySecureId(categoryId, faqCategoryRepository, "Category not found");

        if (!item.getFaqCategoryId().equals(category.getId())) {
            throw new BadRequestException("Faq not belong to this category");
        }

        // update
        converter.convertToUpdateRequest(item, dto);

        // save
        repository.save(item);
    }

    @Override
    public void deleteData(String categoryId, String itemId) throws BadRequestException {
        Faq item = HandlerRepository.getEntityBySecureId(itemId, repository, "Faq not found");
        FaqCategory category = HandlerRepository.getEntityBySecureId(categoryId, faqCategoryRepository, "Category not found");

        if (!item.getFaqCategoryId().equals(category.getId())) {
            throw new BadRequestException("Faq not belong to this category");
        }
        // delete data
        if (!repository.existsById(item.getId())) {
            throw new BadRequestException("Faq not found");
        } else {
            repository.deleteById(item.getId());
        }
    }
}
