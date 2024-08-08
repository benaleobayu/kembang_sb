package com.bca.byc.convert.cms;

import com.bca.byc.entity.Interest;
import com.bca.byc.model.api.InterestCreateRequest;
import com.bca.byc.model.api.InterestDetailResponse;
import com.bca.byc.model.api.InterestUpdateRequest;
import com.bca.byc.model.cms.InterestCategoryDetailResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InterestDTOConverter {

    private ModelMapper modelMapper;

    private InterestCategoryDTOConverter categoryDTOConverter;

    // for get data
    public InterestDetailResponse convertToListResponse(Interest data){
        InterestDetailResponse dto = modelMapper.map(data, InterestDetailResponse.class);

        List<InterestCategoryDetailResponse> dataDTOs = data.getCategories().stream()
                .map(categoryDTOConverter::convertToListResponse)
                .collect(Collectors.toList());

        // set category
        dto.setCategories(dataDTOs);

        return dto;
    }

    // for create data
    public Interest convertToCreateRequest(InterestCreateRequest dto) {
        Interest data = modelMapper.map(dto, Interest.class);

        return data;
    }

    // for update data
    public void convertToUpdateRequest(Interest entity, @Valid InterestUpdateRequest dto){

        modelMapper.map(dto, entity);

        // set updatedAt
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
