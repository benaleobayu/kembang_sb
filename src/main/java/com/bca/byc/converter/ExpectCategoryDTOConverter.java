package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.ExpectCategoryCreateUpdateRequest;
import com.bca.byc.model.ExpectCategoryIndexResponse;
import com.bca.byc.model.PublicExpectCategoryDetailResponse;
import com.bca.byc.model.PublicExpectItemDetailResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@AllArgsConstructor
public class ExpectCategoryDTOConverter {

    private ModelMapper modelMapper;

    public ExpectCategoryIndexResponse convertToIndexResponse(ExpectCategory data) {
        GlobalConverter converter = new GlobalConverter();
        ExpectCategoryIndexResponse dto = new ExpectCategoryIndexResponse();
        dto.setName(data.getName());
        dto.setStatus(data.getIsActive());
        dto.setOrders(data.getOrders());
        List<String> subCategories = new ArrayList<>();
        for (ExpectItem expectItem : data.getExpectItems()) {
            if (expectItem.getIsDeleted().equals(false) && !expectItem.getName().equals("Other")) {
                subCategories.add(expectItem.getName());
            }
        }
        dto.setSubCategories(subCategories);
        converter.CmsIDTimeStampResponseAndId(dto, data);
        return dto;
    }

    // for get data public on app
    public PublicExpectCategoryDetailResponse convertToListResponse(ExpectCategory data) {
        PublicExpectCategoryDetailResponse dto = new PublicExpectCategoryDetailResponse();
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setName(data.getName());
        dto.setIsOther(data.getIsOther() != null && data.getIsOther());

        List<PublicExpectItemDetailResponse> subCategories = new ArrayList<>();
        for (ExpectItem expectItem : data.getExpectItems()) {
            PublicExpectItemDetailResponse sub = new PublicExpectItemDetailResponse();
            sub.setId(expectItem.getSecureId());
            sub.setIndex(expectItem.getId());
            sub.setName(expectItem.getName());
            sub.setIsOther(expectItem.getIsOther() != null && expectItem.getIsOther());
            subCategories.add(sub);
        }

        subCategories.sort(Comparator.comparing(
                PublicExpectItemDetailResponse::getName,
                Comparator.comparing(name -> "Other".equals(name) ? 1 : 0)
        ).thenComparing(PublicExpectItemDetailResponse::getName));

        dto.setExpectItems(subCategories);
        return dto;
    }



    public ExpectCategoryIndexResponse convertToDetailResponse(ExpectCategory data) {
        GlobalConverter converter = new GlobalConverter();
        ExpectCategoryIndexResponse dto = new ExpectCategoryIndexResponse();
        dto.setName(data.getName());
        dto.setStatus(data.getIsActive());
        dto.setOrders(data.getOrders());
        List<String> subCategories = new ArrayList<>();
        for (ExpectItem expectItem : data.getExpectItems()) {
            if (expectItem.getIsDeleted().equals(false) && !expectItem.getName().equals("Other")) {
                subCategories.add(expectItem.getName());
            }
        }
        dto.setSubCategories(subCategories);
        converter.CmsIDTimeStampResponseAndId(dto, data);
        // return
        return dto;
    }


    // for create data

    public ExpectCategory convertToCreateRequest(@Valid ExpectCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        ExpectCategory data = modelMapper.map(dto, ExpectCategory.class);
        // return
        return data;
    }
    // for update data

    public void convertToUpdateRequest(ExpectCategory data, @Valid ExpectCategoryCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
