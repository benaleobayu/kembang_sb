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
        setLogToDto(dto, null, data);
        return dto;
    }

    // for get data
    public LogRequestDetailResponse convertToListResponse(LogRequest data) {
        LogRequestDetailResponse dto = new LogRequestDetailResponse();
        setLogToDto(null, dto, data);
        return dto;
    }

    // -- parser --
    private void setLogToDto(LogRequestIndexResponse indexDto, LogRequestDetailResponse detailDto, LogRequest data) {
        if (indexDto != null) {
            indexDto.setId(data.getSecureId());
            indexDto.setIndex(data.getId());

            indexDto.setAdminName(data.getNameCreatedBy());
            indexDto.setAdminId(data.getIdCreatedBy());
            indexDto.setNote(data.getNote());
            indexDto.setModelId(data.getModelId());
            indexDto.setModelType(data.getModelType());

            indexDto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        } else {
            detailDto.setId(data.getSecureId());

            detailDto.setAdminName(data.getNameCreatedBy());
            detailDto.setAdminId(data.getIdCreatedBy());
            detailDto.setNote(data.getNote());
            detailDto.setModelId(data.getModelId());
            detailDto.setModelType(data.getModelType());

            detailDto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        }
    }
}
