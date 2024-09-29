package com.bca.byc.converter;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Branch;
import com.bca.byc.model.BranchCreateUpdateRequest;
import com.bca.byc.model.BranchDetailResponse;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BranchDTOConverter {

    private ModelMapper modelMapper;
    // for get data
    public BranchDetailResponse convertToListResponse(Branch data) {
        // mapping Entity with DTO Entity
        BranchDetailResponse dto = new BranchDetailResponse();
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setCode(data.getCode());
        dto.setName(data.getName());
        dto.setAddress(data.getAddress());
        dto.setPhone(data.getPhone());
        dto.setStatus(data.isActive());

        dto.setLocation(data.getLocation() != null ? data.getLocation().getName() : null);
        dto.setKanwil(data.getKanwil() != null ? data.getKanwil().getName() : null);

        // Use DataFormatter here
        GlobalConverter conv = new GlobalConverter();
        conv.CmsIDTimeStampResponse(dto, data);
        // return
        return dto;
    }

    // for create data
    public Branch convertToCreateRequest(@Valid BranchCreateUpdateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        Branch data = modelMapper.map(dto, Branch.class);
        // set created_at
        data.setCreatedAt(LocalDateTime.now());
        data.setCreatedBy(admin);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Branch data, @Valid BranchCreateUpdateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);

        data.setActive(dto.getStatus());
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
        data.setUpdatedBy(admin);
    }
}
