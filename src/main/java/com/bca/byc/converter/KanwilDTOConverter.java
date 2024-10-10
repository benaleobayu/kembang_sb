package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Kanwil;
import com.bca.byc.model.*;

import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.security.util.ContextPrincipal;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

import static com.bca.byc.converter.parsing.GlobalConverter.CmsIDTimeStampResponse;

@Component
@AllArgsConstructor
public class KanwilDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public KanwilListResponse convertToListResponse(Kanwil data) {
        // mapping Entity with DTO Entity
        KanwilListResponse dto = new KanwilListResponse();
        mapKanwilToDto(data, dto);

        GlobalConverter converter = new GlobalConverter();
        CmsIDTimeStampResponse(dto, data);
        // return
        return dto;
    }

     // for get data
    public KanwilDetailResponse convertToDetailResponse(Kanwil data) {
        // mapping Entity with DTO Entity
        KanwilDetailResponse dto = new KanwilDetailResponse();
        mapKanwilToDto(data, dto);

        GlobalConverter.CmsIDTimeStampResponse(dto, data);
        // return
        return dto;
    }

    // for create data
    public Kanwil convertToCreateRequest(@Valid KanwilCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Kanwil data = new Kanwil();
        data.setName(dto.getName());
        data.setCode(dto.getCode());
        data.setAddress(dto.getAddress());
        data.setPhone(dto.getPhone());
        data.setIsActive(dto.getStatus());
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Kanwil data, @Valid KanwilCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        data.setName(dto.getName());
        data.setCode(dto.getCode());
        data.setAddress(dto.getAddress());
        data.setPhone(dto.getPhone());
        data.setIsActive(dto.getStatus());
    }

    private <T> void mapKanwilToDto(Kanwil data, T dto) {
        if (dto instanceof KanwilListResponse listDto) {
            listDto.setCode(data.getCode());
            listDto.setName(data.getName());
            listDto.setAddress(data.getAddress());
            listDto.setPhone(data.getPhone());
            listDto.setLocation(data.getLocation() != null ? data.getLocation().getName() : null);
            listDto.setStatus(data.getIsActive());
        } else if (dto instanceof KanwilDetailResponse detailDto) {
            detailDto.setCode(data.getCode());
            detailDto.setName(data.getName());
            detailDto.setAddress(data.getAddress());
            detailDto.setPhone(data.getPhone());
            detailDto.setLocationId(data.getLocation() != null ? data.getLocation().getSecureId() : null);
            detailDto.setLocationName(data.getLocation() != null ? data.getLocation().getName() : null);
            detailDto.setStatus(data.getIsActive());
        }
    }


}
