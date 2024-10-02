package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Faq;
import com.bca.byc.entity.FaqCategory;
import com.bca.byc.model.FaqCategoryCreateRequest;
import com.bca.byc.model.FaqCategoryDetailResponse;
import com.bca.byc.model.FaqCategoryUpdateRequest;
import com.bca.byc.model.FaqDetailResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FaqCategoryDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public FaqCategoryDetailResponse convertToListResponse(FaqCategory data) {
        GlobalConverter converter = new GlobalConverter();
        // mapping Entity with DTO Entity
        FaqCategoryDetailResponse dto = new FaqCategoryDetailResponse();
        dto.setName(data.getName());
        dto.setDescription(data.getDescription());
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        converter.CmsIDTimeStampResponse(dto, data); // timestamp and id

        List<FaqDetailResponse> listFaq = new ArrayList<>();
        for (Faq faq : data.getFaqs()) {
            FaqDetailResponse faqDetailResponse = new FaqDetailResponse();
            faqDetailResponse.setQuestion(faq.getQuestion());
            faqDetailResponse.setAnswer(faq.getAnswer());
            faqDetailResponse.setDescription(faq.getDescription());
            faqDetailResponse.setOrders(faq.getOrders());
            faqDetailResponse.setStatus(faq.getIsActive());
            converter.CmsIDTimeStampResponse(faqDetailResponse, faq); // timestamp and id
            listFaq.add(faqDetailResponse);
        }
        dto.setFaqs(listFaq);
        // return
        return dto;
    }

    // for create data
    public FaqCategory convertToCreateRequest(@Valid FaqCategoryCreateRequest dto) {
        // mapping DTO Entity with Entity
        FaqCategory data = modelMapper.map(dto, FaqCategory.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(FaqCategory data, @Valid FaqCategoryUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
