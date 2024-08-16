package com.bca.byc.convert;

import com.bca.byc.entity.Interest;
import com.bca.byc.model.api.InterestModelDTO;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class InterestDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public InterestModelDTO.DetailResponse convertToListResponse(Interest data) {
        // mapping Entity with DTO Entity
        InterestModelDTO.DetailResponse dto = modelMapper.map(data, InterestModelDTO.DetailResponse.class);
        // Use DataFormatter here
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
    }

    // for create data
    public Interest convertToCreateRequest(@Valid InterestModelDTO.CreateRequest dto) {
        // mapping DTO Entity with Entity
        Interest data = modelMapper.map(dto, Interest.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Interest data, @Valid InterestModelDTO.UpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
