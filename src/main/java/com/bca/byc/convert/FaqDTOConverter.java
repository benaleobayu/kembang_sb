package com.bca.byc.convert;

import com.bca.byc.entity.Faq;
import com.bca.byc.model.FaqCreateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqUpdateRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class FaqDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public FaqDetailResponse convertToListResponse(Faq data) {
        // mapping Entity with DTO Entity
        FaqDetailResponse dto = modelMapper.map(data, FaqDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Faq convertToCreateRequest(@Valid FaqCreateRequest dto) {
        // mapping DTO Entity with Entity
        Faq data = modelMapper.map(dto, Faq.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Faq data, @Valid FaqUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
