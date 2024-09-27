package com.bca.byc.converter;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.entity.PreRegisterLog;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.AdminType;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.model.PreRegisterCreateUpdateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.repository.PreRegisterLogRepository;
import com.bca.byc.response.RejectRequest;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PreRegisterDTOConverter {

    private PreRegisterLogRepository logRepository;

    private ModelMapper modelMapper;

    // for get data
    public PreRegisterDetailResponse convertToListResponse(PreRegister data) {
        // mapping Entity with DTO Entity
        PreRegisterDetailResponse dto = new PreRegisterDetailResponse();
        dto.setId(data.getSecureId());
        dto.setName(data.getName());
        dto.setEmail(data.getEmail());
        dto.setPhone(data.getPhone());
        dto.setMemberBankAccount(data.getMemberBankAccount());
        dto.setParentBankAccount(data.getParentBankAccount());
        dto.setMemberCin(data.getMemberCin());
        dto.setParentCin(data.getParentCin());

        dto.setMemberBirthdate(Formatter.formatLocalDate(data.getMemberBirthdate()));
        dto.setParentBirthdate(Formatter.formatLocalDate(data.getParentBirthdate()));
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
    public PreRegister convertToCreateRequest(@Valid PreRegisterCreateUpdateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        PreRegister data = modelMapper.map(dto, PreRegister.class);

        CreateUpdateSameConvert(data,
                dto.getName(),
                dto.getPicName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getMemberBankAccount(),
                dto.getParentBankAccount(),
                dto.getMemberCin(),
                dto.getParentCin(),
                dto);

        data.setCreatedBy(admin);
        System.out.println(admin);

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
            case MANAGER:
                data.setStatusApproval(AdminApprovalStatus.MGR_APPROVED);
                break;
            default:
                data.setStatusApproval(AdminApprovalStatus.PENDING);
                break;
        }

        data.setStatus(true);

        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(PreRegister data, @Valid PreRegisterCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);

        CreateUpdateSameConvert(data,
                dto.getName(),
                dto.getPicName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getMemberBankAccount(),
                dto.getParentBankAccount(),
                dto.getMemberCin(),
                dto.getParentCin(),
                dto);

        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // set updated_by
        data.setUpdatedBy(data.getCreatedBy());
    }


    public void convertToApprovalRequest(PreRegister data, AppAdmin admin) {
        PreRegisterLog log = new PreRegisterLog();
        log.setStatus(LogStatus.APPROVED);
        log.setPreRegister(data);
        log.setMessage("Approved by " + admin.getName());
        log.setUpdatedBy(admin.getName());
        logRepository.save(log);

        AdminType typeAdmin = admin.getType();
        switch (typeAdmin) {
            case SUPERADMIN:
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
                break;
            case OPERATIONAL:
                data.setStatusApproval(AdminApprovalStatus.OPT_APPROVED);
                break;
            case SUPERVISOR:
                data.setStatusApproval(AdminApprovalStatus.SPV_APPROVED);
                break;
            case MANAGER:
                data.setStatusApproval(AdminApprovalStatus.MGR_APPROVED);
                break;
            default:
                data.setStatusApproval(AdminApprovalStatus.PENDING);
                break;
        }
    }

    public void convertToRejectRequest(PreRegister data, RejectRequest reason, AppAdmin admin) {
        PreRegisterLog log = new PreRegisterLog();
        log.setStatus(LogStatus.APPROVED);
        log.setPreRegister(data);
        log.setMessage(reason.message());
        log.setUpdatedBy(admin.getName());
        logRepository.save(log);

        data.setDeleted(true);
        data.setEmail(data.getEmail().concat("_rejected"));
        data.setStatusApproval(AdminApprovalStatus.REJECTED);

    }

    private void CreateUpdateSameConvert(PreRegister data,
                                         String name,
                                         String picName,
                                         String email,
                                         String phone,
                                         String memberBankAccount,
                                         String parentBankAccount,
                                         String memberCin,
                                         String parentCin,
                                         @Valid PreRegisterCreateUpdateRequest dto) {
        data.setName(StringUtils.capitalize(name));
        data.setPicName(StringUtils.capitalize(picName));
        data.setEmail(email.toLowerCase());
        data.setPhone(phone.replaceAll("[^0-9]", ""));
        data.setMemberBankAccount(memberBankAccount.replaceAll("[^0-9]", ""));
        data.setParentBankAccount(parentBankAccount.replaceAll("[^0-9]", ""));
        data.setMemberCin(memberCin.replaceAll("[^0-9]", ""));
        data.setParentCin(parentCin.replaceAll("[^0-9]", ""));
    }
}
