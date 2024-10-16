package com.bca.byc.converter;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.BlacklistKeyword;
import com.bca.byc.model.BlacklistIndexResponse;
import com.bca.byc.model.BlacklistKeywordCreateUpdateRequest;
import com.bca.byc.model.BlacklistKeywordDetailResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.bca.byc.converter.parsing.GlobalConverter.CmsIDTimeStampResponseAndId;

@Component
@AllArgsConstructor
public class BlacklistKeywordDTOConverter {

    private ModelMapper modelMapper;

    // for get data index
    public BlacklistIndexResponse convertToListResponse(BlacklistKeyword data) {
        // mapping Entity with DTO Entity
        BlacklistIndexResponse dto = new BlacklistIndexResponse();
        dto.setKeyword(data.getKeyword());
        dto.setStatus(data.getIsActive());
        dto.setOrders(data.getOrders());
        CmsIDTimeStampResponseAndId(dto, data);

        // return
        return dto;
    }

    // for get data detail
    public BlacklistKeywordDetailResponse convertToDetailResponse(BlacklistKeyword data) {
        // mapping Entity with DTO Entity
        BlacklistKeywordDetailResponse dto = new BlacklistKeywordDetailResponse();
        dto.setId(data.getSecureId());
        dto.setKeyword(data.getKeyword());
        dto.setStatus(data.getIsActive());

        // return
        return dto;
    }

    // for create data
    public BlacklistKeyword convertToCreateRequest(@Valid BlacklistKeywordCreateUpdateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        BlacklistKeyword data = modelMapper.map(dto, BlacklistKeyword.class);
        data.setKeyword(dto.getKeyword().toLowerCase());
        data.setIsActive(dto.getStatus());

        data.setCreatedAt(LocalDateTime.now());
        data.setUpdatedAt(LocalDateTime.now());
        data.setCreatedBy(admin);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(BlacklistKeyword data, @Valid BlacklistKeywordCreateUpdateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        data.setKeyword(dto.getKeyword().toLowerCase());
        data.setIsActive(dto.getStatus());
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
        data.setUpdatedBy(admin);
    }


}
