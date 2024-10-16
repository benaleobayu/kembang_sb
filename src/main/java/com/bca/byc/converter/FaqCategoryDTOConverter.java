package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Faq;
import com.bca.byc.entity.FaqCategory;
import com.bca.byc.model.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FaqCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public FaqCategoryDetailResponse convertToListResponse(FaqCategory data) {
        // mapping Entity with DTO Entity
        FaqCategoryDetailResponse dto = new FaqCategoryDetailResponse();
        dto.setName(data.getName() != null ? data.getName() : null);
        dto.setDescription(data.getDescription() != null ? data.getDescription() : null);
        dto.setOrders(data.getOrders() != null ? data.getOrders() : null);
        dto.setStatus(data.getIsActive() != null ? data.getIsActive() : null);
        GlobalConverter.CmsIDTimeStampResponseAndId(dto, data); // timestamp and id

        List<FaqDetailResponse> listFaq = new ArrayList<>();
        for (Faq faq : data.getFaqs()) {
            FaqDetailResponse faqDetailResponse = new FaqDetailResponse();
            faqDetailResponse.setQuestion(faq.getQuestion());
            faqDetailResponse.setAnswer(faq.getAnswer());
            faqDetailResponse.setOrders(faq.getOrders());
            faqDetailResponse.setStatus(faq.getIsActive());
            GlobalConverter.CmsIDTimeStampResponseAndId(faqDetailResponse, faq); // timestamp and id
            listFaq.add(faqDetailResponse);
        }
        dto.setFaqs(listFaq);
        // return
        return dto;
    }
    // for get data
    public FaqCategoryIndexResponse convertToIndexResponse(FaqCategory data) {
        // mapping Entity with DTO Entity
        FaqCategoryIndexResponse dto = new FaqCategoryIndexResponse();
        dto.setName(data.getName());
        dto.setDescription(data.getDescription());
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        GlobalConverter.CmsIDTimeStampResponseAndId(dto, data); // timestamp and id

        List<String> listFaq = data.getFaqs() != null ? data.getFaqs().stream()
                .map(Faq::getName)
                .collect(Collectors.toList()) : new ArrayList<>();

        dto.setFaqs(listFaq);
        // return
        return dto;
    }
    // for get data
    public FaqCategoryDetailResponse convertToDetailResponse(FaqCategory data) {
        // mapping Entity with DTO Entity
        FaqCategoryDetailResponse dto = new FaqCategoryDetailResponse();
        dto.setName(data.getName());
        dto.setDescription(data.getDescription());
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        GlobalConverter.CmsIDTimeStampResponseAndId(dto, data); // timestamp and id

        List<FaqDetailResponse> listFaq = new ArrayList<>();
        for (Faq faq : data.getFaqs()) {
            FaqDetailResponse faqDetailResponse = new FaqDetailResponse();
            faqDetailResponse.setQuestion(faq.getQuestion());
            faqDetailResponse.setAnswer(faq.getAnswer());
            faqDetailResponse.setOrders(faq.getOrders());
            faqDetailResponse.setStatus(faq.getIsActive());
            GlobalConverter.CmsIDTimeStampResponseAndId(faqDetailResponse, faq); // timestamp and id
            listFaq.add(faqDetailResponse);
        }
        dto.setFaqs(listFaq);
        // return
        return dto;
    }


    // for create data
    public FaqCategory convertToCreateRequest(@Valid FaqCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        FaqCategory data = modelMapper.map(dto, FaqCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(FaqCategory data, @Valid FaqCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
