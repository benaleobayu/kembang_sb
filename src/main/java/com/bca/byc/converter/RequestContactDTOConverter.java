package com.bca.byc.converter;

import com.bca.byc.entity.AppUserRequestContact;
import com.bca.byc.model.RequestContactUpdateRequest;
import com.bca.byc.model.RequestContactDetailResponse;
import com.bca.byc.model.RequestContactIndexResponse;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.bca.byc.converter.parsing.GlobalConverter.CmsIDTimeStampResponseAndId;

@Component
@AllArgsConstructor
public class RequestContactDTOConverter {

    private ModelMapper modelMapper;

    public RequestContactIndexResponse convertToIndexResponse(AppUserRequestContact data) {
        RequestContactIndexResponse dto = new RequestContactIndexResponse();
        dto.setName(data.getUser().getAppUserDetail().getName());
        dto.setEmail(data.getUser().getEmail());
        dto.setPhone(data.getUser().getAppUserDetail().getPhone());
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setStatus(data.getStatus());
        CmsIDTimeStampResponseAndId(dto, data); // timestamp and id
        return dto;
    }

    // for get data
    public RequestContactDetailResponse convertToListResponse(AppUserRequestContact data) {
        // mapping Entity with DTO Entity
        RequestContactDetailResponse dto = new RequestContactDetailResponse();
        dto.setName(data.getUser().getAppUserDetail().getName());
        dto.setEmail(data.getUser().getEmail());
        dto.setPhone(data.getUser().getAppUserDetail().getPhone());
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setStatus(data.getStatus());
        dto.setMessages(data.getMessages());
        CmsIDTimeStampResponseAndId(dto, data); // timestamp and id
        // return
        return dto;
    }

    // for update data
    public void convertToUpdateRequest(AppUserRequestContact data, @Valid RequestContactUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        data.setIsActive(dto.getStatus());
        data.setUpdatedAt(LocalDateTime.now());
    }
}
