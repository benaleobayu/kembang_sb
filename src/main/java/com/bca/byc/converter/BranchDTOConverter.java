package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Branch;
import com.bca.byc.model.BranchCreateUpdateRequest;
import com.bca.byc.model.BranchDetailResponse;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

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
        dto.setStatus(data.getIsActive());

        dto.setLocationId(data.getLocation() != null ? data.getLocation().getSecureId() : null);
        dto.setLocationName(data.getLocation() != null ? data.getLocation().getName() : null);
        dto.setKanwilId(data.getKanwil() != null ? data.getKanwil().getSecureId() : null);
        dto.setKanwilName(data.getKanwil() != null ? data.getKanwil().getName() : null);

        // Use DataFormatter here
        GlobalConverter conv = new GlobalConverter();
        conv.CmsIDTimeStampResponseAndId(dto, data);
        // return
        return dto;
    }

    // for create data
    public Branch convertToCreateRequest(@Valid BranchCreateUpdateRequest dto) {
        Branch data = new Branch();
        data.setCode(dto.code());
        data.setName(dto.name());
        data.setAddress(dto.address());
        data.setPhone(dto.phone());
        data.setIsActive(dto.status());
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Branch data, @Valid BranchCreateUpdateRequest dto) {
        data.setCode(dto.code());
        data.setName(dto.name());
        data.setAddress(dto.address());
        data.setPhone(dto.phone());
        data.setIsActive(dto.status());
    }
}
