package com.bca.byc.converter;

import com.bca.byc.entity.LogRequest;
import com.bca.byc.model.LogRequestDetailResponse;
import com.bca.byc.model.LogRequestIndexResponse;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.bca.byc.converter.parsing.GlobalConverter.CmsIDTimeStampResponseAndId;

@Component
@AllArgsConstructor
public class LogRequestDTOConverter {

    private ModelMapper modelMapper;

    public LogRequestIndexResponse convertToIndexResponse(LogRequest data) {
        LogRequestIndexResponse dto = new LogRequestIndexResponse();
        dto.setAdmin(data.getAdmin().getName());
        dto.setNote(data.getNote());
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setStatus(data.getTo());
        CmsIDTimeStampResponseAndId(dto, data); // timestamp and id
        return dto;
    }

    // for get data
    public LogRequestDetailResponse convertToListResponse(LogRequest data) {
        // mapping Entity with DTO Entity
        LogRequestDetailResponse dto = new LogRequestDetailResponse();
        dto.setAdmin(data.getAdmin().getName());
        dto.setNote(data.getNote());
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setStatus(data.getTo());
        CmsIDTimeStampResponseAndId(dto, data); // timestamp and id
        // return
        return dto;
    }
}
