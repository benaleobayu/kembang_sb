package com.bca.byc.convert.cms;

import com.bca.byc.entity.Business;
import com.bca.byc.model.api.BusinessCreateRequest;
import com.bca.byc.model.api.BusinessDetailResponse;
import com.bca.byc.model.api.BusinessUpdateRequest;
import com.bca.byc.model.cms.BusinessCategoryDetailResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BusinessDTOConverter {

    private ModelMapper modelMapper;

    private BusinessCategoryDTOConverter categoryDTOConverter;

    // for get data
    public BusinessDetailResponse convertToListResponse(Business data){
        BusinessDetailResponse dto = modelMapper.map(data, BusinessDetailResponse.class);

        List<BusinessCategoryDetailResponse> dataDTOs = data.getCategories().stream()
                .map(categoryDTOConverter::convertToListResponse)
                .collect(Collectors.toList());

        // set category
        dto.setCategories(dataDTOs);

        return dto;
    }

    // for create data
    public Business convertToCreateRequest(BusinessCreateRequest dto) {
        Business data = modelMapper.map(dto, Business.class);

        return data;
    }

    // for update data
    public void convertToUpdateRequest(Business entity, @Valid BusinessUpdateRequest dto){

        modelMapper.map(dto, entity);

        // set updatedAt
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
