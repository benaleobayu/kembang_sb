package com.bca.byc.convert;

import com.bca.byc.entity.Faq;
import com.bca.byc.model.FaqModelDTO;
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
    public FaqModelDTO.FaqDetailResponse convertToListResponse(Faq data) {
        // mapping Entity with DTO Entity
        FaqModelDTO.FaqDetailResponse dto = modelMapper.map(data, FaqModelDTO.FaqDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Faq convertToCreateRequest(@Valid FaqModelDTO.FaqCreateRequest dto) {
        // mapping DTO Entity with Entity
        Faq data = modelMapper.map(dto, Faq.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Faq data, @Valid FaqModelDTO.FaqUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
