package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.entity.elastic.PreRegisterElastic;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.model.Elastic.PreRegisterIndexElasticResponse;
import com.bca.byc.repository.BranchRepository;
import com.bca.byc.repository.LogUserManagementRepository;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.response.RejectRequest;
import com.bca.byc.service.email.EmailDTORequest;
import com.bca.byc.service.email.EmailService;
import com.bca.byc.util.helper.Formatter;
import jakarta.mail.MessagingException;
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
    private final EmailService emailService;

    private LogUserManagementRepository logRepository;

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
        dto.setMemberType(data.getMemberType() != null ? data.getMemberType().toString() : null);
        dto.setParentType(data.getParentType() != null ? data.getParentType().toString() : null);
        dto.setOrders(data.getOrders());
        dto.setApprovalStatus(data.getStatusApproval().name());

        Branch branch = data.getBranch();
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
        GlobalConverter.CmsIDTimeStampResponseAndId(dto, data); // timestamp and id
        // return
        return dto;
    }
    // for get data
    public PreRegisterIndexElasticResponse convertToListResponseElastic(PreRegisterElastic data) {
        PreRegisterIndexElasticResponse dto = new PreRegisterIndexElasticResponse();
        dto.setId(data.getSecureId());
        dto.setName(data.getName());
        dto.setEmail(data.getEmail());
        dto.setBranchId(data.getBranchId());
        dto.setBranchName(data.getBranchName());
//        dto.setPhone(data.getPhone());
//        dto.setMemberBankAccount(data.getMemberBankAccount());
//        dto.setParentBankAccount(data.getParentBankAccount());
//        dto.setMemberCin(data.getMemberCin());
//        dto.setParentCin(data.getParentCin());
//        dto.setMemberType(data.getMemberType() != null ? data.getMemberType() : null);
//        dto.setParentType(data.getParentType() != null ? data.getParentType() : null);
//        dto.setOrders(data.getOrders());
//        dto.setApprovalStatus(data.getStatusApproval());

//        BranchCodeResponse branchCodeResponse = new BranchCodeResponse();
//        if (data.getBranchId() != null && data.getBranchName() != null) {
//            branchCodeResponse.setId(data.getBranchId());
//            branchCodeResponse.setName(data.getBranchName());
//        }
//        dto.setBranchCode(branchCodeResponse);
//        dto.setPicName(data.getPicName());
//
//        dto.setMemberBirthdate(data.getMemberBirthdate() != null ? Formatter.formatLocalDate(data.getMemberBirthdate()) : null);
//        dto.setParentBirthdate(data.getParentBirthdate() != null ? Formatter.formatLocalDate(data.getParentBirthdate()) : null);

        return dto;
    }



    // for create data
    public PreRegister convertToCreateRequest(@Valid PreRegisterCreateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        PreRegister data = new PreRegister();
        convertToCreatePreRegister(data, dto);
        data.setCreatedBy(admin);
        String roleEquals = admin.getRole().getName();

        switch (roleEquals) {
            case "SUPERADMIN":
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
                break;
            case "ADMIN-OPERATIONAL":
                data.setStatusApproval(AdminApprovalStatus.OPT_APPROVED);
                break;
            case "ADMIN-SUPERVISOR":
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
                break;
            case "ADMIN-MANAGER":
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


    public void convertToApprovalRequest(PreRegister data, AppAdmin admin) throws MessagingException {
        UserManagementLog log = new UserManagementLog();
        log.setStatus(LogStatus.APPROVED);
        log.setPreRegister(data);
        log.setMessage("Approved by " + admin.getName());
        log.setUpdatedBy(admin);
        logRepository.save(log);

        EmailDTORequest emailData = new EmailDTORequest();
        emailData.setEmail(data.getEmail());
        emailData.setSubject("Pre-Register Approval");
        emailData.setCardNumber(data.getMemberBankAccount());
        emailData.setName(data.getName());

        String typeAdmin = admin.getRole().getName();
        switch (typeAdmin) {
            case "SUPERADMIN":
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
                emailService.sendApprovalPreRegister(emailData);
                break;
            case "ADMIN-OPERATIONAL":
                data.setStatusApproval(AdminApprovalStatus.OPT_APPROVED);
                break;
            case "ADMIN-SUPERVISOR":
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
                emailService.sendApprovalPreRegister(emailData);
                break;
            case "ADMIN-MANAGER":
                data.setStatusApproval(AdminApprovalStatus.APPROVED);
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
        data.setMemberType(dto.getMemberType() != null ? dto.getMemberType() : null);
        data.setParentType(dto.getParentType() != null ? dto.getParentType() : null);
        Branch branch = branchRepository.findBySecureId(dto.getBranchCode()).orElse(null);
        data.setBranch(branch);
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
        data.setMemberType(dto.getMemberType() != null ? dto.getMemberType() : null);
        Branch branch = branchRepository.findBySecureId(dto.getBranchCode()).orElse(null);
        data.setBranch(branch);
        data.setPicName(StringUtils.capitalize(dto.getPicName()));
    }


    public boolean emailExists(String email, PreRegisterRepository repository) {
        return repository.existsByEmail(email);
    }

    public boolean cinExists(String cin, PreRegisterRepository repository) {
        return repository.existsByMemberCin(cin);
    }

}
