package com.bca.byc.converter;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.AdminType;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PreRegisterDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public PreRegisterDetailResponse convertToListResponse(PreRegister data) {
        // mapping Entity with DTO Entity
        PreRegisterDetailResponse dto = modelMapper.map(data, PreRegisterDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));

        // created & updated by
        dto.setCreatedBy(data.getCreatedBy().getUsername());
        if (data.getUpdatedBy() != null) {
            dto.setUpdatedBy(data.getUpdatedBy().getUsername());
        }

        // return
        return dto;
    }

    // for create data
    public PreRegister convertToCreateRequest(@Valid PreRegisterCreateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        PreRegister data = modelMapper.map(dto, PreRegister.class);

        data.setCreatedBy(admin);

        AdminType typeEquals = admin.getType();

        switch (typeEquals) {
            case SUPERADMIN:
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
                break;
            case OPERATIONAL:
                data.setStatusApproval(AdminApprovalStatus.OPT_APPROVED);
                break;
            case SUPERVISOR:
                data.setStatusApproval(AdminApprovalStatus.SPV_APPROVED);
                break;
            default:
                data.setStatusApproval(AdminApprovalStatus.PENDING);
                break;
        }

        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(PreRegister data, @Valid PreRegisterUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // set updated_by
        data.setUpdatedBy(data.getCreatedBy());
    }
}
