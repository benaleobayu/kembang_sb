package com.bca.byc.converter;

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
        BranchDetailResponse dto = modelMapper.map(data, BranchDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Branch convertToCreateRequest(@Valid BranchCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Branch data = modelMapper.map(dto, Branch.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Branch data, @Valid BranchCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
