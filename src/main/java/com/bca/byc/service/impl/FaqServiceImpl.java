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
    private final FaqRepository faqRepository;
    private final FaqDTOConverter converter;

    @Override
    public ResultPageResponseDTO<FaqIndexResponse> FaqItemIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String categoryId) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        FaqCategory faqCategoryId = faqCategoryId(categoryId);

        Page<Faq> pageResult = faqRepository.getFaqItemIndex(set.keyword(), set.pageable(), faqCategoryId.getId());
        List<FaqIndexResponse> dtos = pageResult.stream().map((c) -> {
            FaqIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public FaqDetailResponse DetailFaqItem(String categoryId, String itemId) throws BadRequestException {
        Faq item = faqItem(itemId);
        FaqCategory category = faqCategory(categoryId);

        if (!item.getFaqCategoryId().getId().equals(category.getId())) {
            throw new BadRequestException("Faq not belong to this category");
        }

        return converter.convertToDetailResponse(item);
    }

    @Override
    public void CreateFaqItem(String categoryId, @Valid FaqCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        FaqCategory category = faqCategory(categoryId);
        // set entity to add with model mapper
        Faq data = converter.convertToCreateRequest(dto);
        data.setFaqCategoryId(category);
        GlobalConverter.CmsAdminCreateAtBy(data, admin);
        // save data
        faqRepository.save(data);
    }

    @Override
    public void UpdateFaqItem(String categoryId, String itemId, FaqCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Faq item = faqItem(itemId);
        FaqCategory category = faqCategory(categoryId);

        if (!item.getFaqCategoryId().getId().equals(category.getId())) {
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
        faqRepository.save(item);
    }

    @Override
    public void DeleteFaqItem(String categoryId, String itemId) throws BadRequestException {
        Faq item = faqItem(itemId);
        FaqCategory category = faqCategory(categoryId);

        if (!item.getFaqCategoryId().getId().equals(category.getId())) {
            throw new BadRequestException("Faq not belong to this category");
        }
        // delete data
        if (!faqRepository.existsById(item.getId())) {
            throw new BadRequestException("Faq not found");
        } else {
            faqRepository.deleteById(item.getId());
        }
    }

    // --- Helper ---

    private FaqCategory faqCategory(String categoryId){
        return HandlerRepository.getIdBySecureId(
                categoryId,
                faqCategoryRepository::findBySecureId,
                projection -> faqCategoryRepository.findById(projection.getId()),
                "Category not found"
        );
    }

     private Faq faqItem(String itemId){
         return HandlerRepository.getIdBySecureId(
                itemId,
                faqRepository::findBySecureId,
                projection -> faqRepository.findById(projection.getId()),
                "Faq not found"
        );
    }

    private FaqCategory faqCategoryId (String categoryId){
         return HandlerRepository.getIdBySecureId(
                categoryId,
                faqCategoryRepository::findByIdAndSecureId,
                projection -> faqCategoryRepository.findById(projection.getId()),
                "Category not found"
        );
    }




}
