package com.bca.byc.converter;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Kanwil;
import com.bca.byc.model.*;

import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.util.helper.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class KanwilDTOConverter {

    private ModelMapper modelMapper;
    private AppAdminRepository adminRepository;

    // for get data
    public KanwilListResponse convertToListResponse(Kanwil data) {
        // mapping Entity with DTO Entity
        KanwilListResponse dto = new KanwilListResponse();
        mapKanwilToDto(data, dto);

        GlobalConverter converter = new GlobalConverter();
        converter.CmsIDTimeStampResponse(dto, data);
        // return
        return dto;
    }

     // for get data
    public KanwilDetailResponse convertToDetailResponse(Kanwil data) {
        // mapping Entity with DTO Entity
        KanwilDetailResponse dto = new KanwilDetailResponse();
        mapKanwilToDto(data, dto);

        GlobalConverter converter = new GlobalConverter();
        converter.CmsIDTimeStampResponse(dto, data);
        // return
        return dto;
    }

    // for create data
    public Kanwil convertToCreateRequest(@Valid KanwilCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Kanwil data = modelMapper.map(dto, Kanwil.class);
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "admin not found");
        // set created_at
        data.setCreatedAt(LocalDateTime.now());
        data.setCreatedBy(admin);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Kanwil data, @Valid KanwilCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        String email = ContextPrincipal.getSecureUserId();
        AppAdmin admin = HandlerRepository.getAdminByEmail(email, adminRepository, "admin not found");
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
        data.setUpdatedBy(admin);
    }

    private <T> void mapKanwilToDto(Kanwil data, T dto) {
        if (dto instanceof KanwilListResponse) {
            KanwilListResponse listDto = (KanwilListResponse) dto;
            listDto.setCode(data.getCode());
            listDto.setName(data.getName());
            listDto.setAddress(data.getAddress());
            listDto.setPhone(data.getPhone());
            listDto.setLocation(data.getLocation() != null ? data.getLocation().getName() : null);
            listDto.setStatus(data.isActive());
        } else if (dto instanceof KanwilDetailResponse) {
            KanwilDetailResponse detailDto = (KanwilDetailResponse) dto;
            detailDto.setCode(data.getCode());
            detailDto.setName(data.getName());
            detailDto.setAddress(data.getAddress());
            detailDto.setPhone(data.getPhone());
            detailDto.setLocation(data.getLocation() != null ? data.getLocation().getName() : null);
            detailDto.setStatus(data.isActive());
        }
    }


}
