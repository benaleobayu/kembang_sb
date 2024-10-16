package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Faq;
import com.bca.byc.entity.FaqCategory;
import com.bca.byc.model.FaqCreateUpdateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.FaqCategoryRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.bca.byc.converter.FaqDTOConverter;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.repository.FaqRepository;
import com.bca.byc.service.FaqService;

@Service
@AllArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final AppAdminRepository adminRepository;

    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqRepository repository;
    private final FaqDTOConverter converter;

    @Override
    public ResultPageResponseDTO<FaqIndexResponse> FaqItemIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String categoryId) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        FaqCategory faqCategoryId = HandlerRepository.getIdBySecureId(
                categoryId,
                faqCategoryRepository::findByIdAndSecureId,
                projection -> faqCategoryRepository.findById(projection.getId()),
                "Category not found"
        );
        Page<Faq> pageResult = repository.getFaqItemIndex(set.keyword(), set.pageable(), faqCategoryId.getId());
        List<FaqIndexResponse> dtos = pageResult.stream().map((c) -> {
            FaqIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public FaqDetailResponse DetailFaqItem(String categoryId, String itemId) throws BadRequestException {
        Faq item = HandlerRepository.getEntityBySecureId(itemId, repository, "Faq not found");
        FaqCategory category = HandlerRepository.getEntityBySecureId(categoryId, faqCategoryRepository, "Category not found");
        if (!item.getFaqCategoryId().equals(category.getId())) {
            throw new BadRequestException("Faq not belong to this category");
        }

        return converter.convertToDetailResponse(item);
    }

    @Override
    public void CreateFaqItem(String categoryId, @Valid FaqCreateUpdateRequest dto) throws BadRequestException {
        FaqCategory category = HandlerRepository.getEntityBySecureId(categoryId, faqCategoryRepository, "Category not found");
        // set entity to add with model mapper
        Faq data = converter.convertToCreateRequest(dto);
        data.setFaqCategoryId(category);
        // save data
        repository.save(data);
    }

    @Override
    public void UpdateFaqItem(String categoryId, String itemId, FaqCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Faq item = HandlerRepository.getEntityBySecureId(itemId, repository, "Faq not found");
        FaqCategory category = HandlerRepository.getEntityBySecureId(categoryId, faqCategoryRepository, "Category not found");

        if (!item.getFaqCategoryId().equals(category.getId())) {
            throw new BadRequestException("Faq not belong to this category");
        }

        // update
        item.setName(dto.getName());
        item.setQuestion(dto.getQuestion());
        item.setAnswer(dto.getAnswer());
        item.setIsActive(dto.getStatus());
        item.setOrders(dto.getOrders());

        GlobalConverter.CmsAdminUpdateAtBy(item, admin);

        // save
        repository.save(item);
    }

    @Override
    public void DeleteFaqItem(String categoryId, String itemId) throws BadRequestException {
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
