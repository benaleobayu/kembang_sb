package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Branch;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.entity.UserManagementLog;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.AdminType;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BranchCodeResponse;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.repository.BranchRepository;
import com.bca.byc.repository.LogUserManagementRepository;
import com.bca.byc.repository.PreRegisterRepository;
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

    private final BranchRepository branchRepository;
    private final PreRegisterRepository repository;
    private LogUserManagementRepository logRepository;
    private ModelMapper modelMapper;

    // for get data
    public PreRegisterDetailResponse convertToListResponse(PreRegister data) {
        GlobalConverter converter = new GlobalConverter();
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
        dto.setType(data.getType() != null ? data.getType().toString() : null);
        dto.setMemberType(data.getMemberType());
        dto.setOrders(data.getOrders());
        dto.setApprovalStatus(data.getStatusApproval());

        Branch branch = data.getBranchCode();
        BranchCodeResponse branchCodeResponse = new BranchCodeResponse();
        if (branch != null) {
            branchCodeResponse.setId(branch.getSecureId());
            branchCodeResponse.setName(branch.getName());
        }

        dto.setBranchCode(branchCodeResponse);
        dto.setPicName(data.getPicName());

        dto.setMemberBirthdate(Formatter.formatLocalDate(data.getMemberBirthdate()));
        dto.setParentBirthdate(Formatter.formatLocalDate(data.getParentBirthdate()));
        // Use DataFormatter here
        converter.CmsIDTimeStampResponse(dto, data); // timestamp and id
        // return
        return dto;
    }

    // for create data
    public PreRegister convertToCreateRequest(@Valid PreRegisterCreateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        PreRegister data = new PreRegister();
        convertToCreatePreRegister(data, dto);
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
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
                break;
            case MANAGER:
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
                break;
            default:
                data.setStatusApproval(AdminApprovalStatus.PENDING);
                break;
        }

        data.setIsActive(true);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(PreRegister data, @Valid PreRegisterUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        String type = "update";
        convertToUpdate(data, dto, repository);

        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // set updated_by
        data.setUpdatedBy(data.getCreatedBy());
    }


    public void convertToApprovalRequest(PreRegister data, AppAdmin admin) {
        UserManagementLog log = new UserManagementLog();
        log.setStatus(LogStatus.APPROVED);
        log.setPreRegister(data);
        log.setMessage("Approved by " + admin.getName());
        log.setUpdatedBy(admin);
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
        UserManagementLog log = new UserManagementLog();
        log.setStatus(LogStatus.APPROVED);
        log.setPreRegister(data);
        log.setMessage(reason.message());
        log.setUpdatedBy(admin);
        logRepository.save(log);

        data.setIsDeleted(true);
        data.setEmail(data.getEmail().concat("_rejected"));
        data.setStatusApproval(AdminApprovalStatus.REJECTED);

    }

    private void convertToCreatePreRegister(PreRegister data,
                                            @Valid PreRegisterCreateRequest dto) {
        data.setName(StringUtils.capitalize(dto.getName()));
        data.setEmail(dto.getEmail().toLowerCase());
        data.setPhone(dto.getPhone().replaceAll("[^0-9]", ""));
        data.setMemberBankAccount(dto.getMemberBankAccount() != null ? dto.getMemberBankAccount().replaceAll("[^0-9]", "") : null);
        data.setParentBankAccount(dto.getParentBankAccount() != null ? dto.getParentBankAccount().replaceAll("[^0-9]", "") : null);
        data.setMemberCin(dto.getMemberCin() != null ? dto.getMemberCin().replaceAll("[^0-9]", "") : null);
        data.setParentCin(dto.getParentCin() != null ? dto.getParentCin().replaceAll("[^0-9]", "") : null);
        data.setMemberBirthdate(dto.getMemberBirthdate());
        data.setParentBirthdate(dto.getParentBirthdate());
        data.setType(dto.getType() != null ? dto.getType() : null);
        Branch branch = branchRepository.findBySecureId(dto.getBranchCode()).orElse(null);
        data.setBranchCode(branch);
        data.setPicName(StringUtils.capitalize(dto.getPicName()));
    }

    private void convertToUpdate(PreRegister data,
                                 @Valid PreRegisterUpdateRequest dto,
                                 PreRegisterRepository repository) {
        data.setName(StringUtils.capitalize(dto.getName()));
        if (!data.getEmail().equals(dto.getEmail())) {
            if (emailExists(dto.getEmail(), repository)) {
                throw new BadRequestException("Email already exists");
            }
            data.setEmail(dto.getEmail().toLowerCase());
        }
        data.setPhone(dto.getPhone().replaceAll("[^0-9]", ""));
        data.setMemberBankAccount(dto.getMemberBankAccount() != null ? dto.getMemberBankAccount().replaceAll("[^0-9]", "") : null);
        data.setParentBankAccount(dto.getParentBankAccount() != null ? dto.getParentBankAccount().replaceAll("[^0-9]", "") : null);
        if (!data.getMemberCin().equals(dto.getMemberCin())) {
            if (cinExists(dto.getMemberCin(), repository)) {
                throw new BadRequestException("Cin data already exists");
            }
            data.setMemberCin(dto.getMemberCin() != null ? dto.getMemberCin().replaceAll("[^0-9]", "") : null);
        }
        data.setParentCin(dto.getParentCin() != null ? dto.getParentCin().replaceAll("[^0-9]", "") : null);
        data.setMemberBirthdate(dto.getMemberBirthdate());
        data.setParentBirthdate(dto.getParentBirthdate());
        data.setType(dto.getType() != null ? dto.getType() : null);
        Branch branch = branchRepository.findBySecureId(dto.getBranchCode()).orElse(null);
        data.setBranchCode(branch);
        data.setPicName(StringUtils.capitalize(dto.getPicName()));
    }


    public boolean emailExists(String email, PreRegisterRepository repository) {
        return repository.existsByEmail(email);
    }

    public boolean cinExists(String cin, PreRegisterRepository repository) {
        return repository.existsByMemberCin(cin);
    }
}
