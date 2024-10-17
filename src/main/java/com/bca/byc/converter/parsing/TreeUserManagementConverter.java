package com.bca.byc.converter.parsing;

import com.bca.byc.entity.*;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.enums.StatusType;
import com.bca.byc.model.BranchCodeResponse;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.util.helper.Formatter;

import java.util.List;

public class TreeUserManagementConverter {

    public static void IndexResponse(
            AppUser data,
            UserManagementListResponse dto
    ){
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setBranchCode(data.getAppUserDetail().getBranchCode() != null ? data.getAppUserDetail().getBranchCode().getName() : null);
        dto.setName(data.getAppUserDetail().getName());
        dto.setBirthDate(data.getAppUserDetail().getMemberBirthdate() != null ?
                Formatter.formatLocalDate(data.getAppUserDetail().getMemberBirthdate()) : null);
        dto.setEmail(data.getEmail().toLowerCase());
        dto.setPhone(data.getAppUserDetail().getPhone());
        dto.setMemberCin(data.getAppUserDetail().getMemberCin());
        dto.setCreatedAt(data.getCreatedAt() != null ? Formatter.formatLocalDateTime(data.getCreatedAt()) : null);
        dto.setCreatedBy(data.getCreatedBy() != null ? data.getEmail() : null);
        dto.setUpdatedAt(data.getAppUserDetail().getUpdatedAt() != null ? Formatter.formatLocalDateTime(data.getAppUserDetail().getUpdatedAt()) : null);
        dto.setUpdatedBy(data.getUpdatedAt() != null ? data.getEmail() : null);
        dto.setApproveAt(data.getAppUserDetail().getApprovedAt() != null ? Formatter.formatLocalDateTime(data.getAppUserDetail().getApprovedAt()) : null);
        dto.setApproveBy(data.getAppUserDetail().getApprovedBy() != null ? data.getAppUserDetail().getApprovedBy() : null);
    }

    public void DetailResponse(
            AppUser data,
            UserManagementDetailResponse dto
    ) {
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setName(data.getAppUserDetail().getName());
        dto.setBirthDate(data.getAppUserDetail().getMemberBirthdate() != null ?
                Formatter.formatLocalDate(data.getAppUserDetail().getMemberBirthdate()) : null);
        dto.setEmail(data.getEmail().toLowerCase());
        dto.setPhone(data.getAppUserDetail().getPhone());
        dto.setMemberCin(data.getAppUserDetail().getMemberCin());
        dto.setMemberCardNumber(data.getAppUserDetail().getMemberBankAccount());
        dto.setType(data.getAppUserDetail().getAccountType());
        dto.setMemberType(data.getAppUserDetail().getMemberType() != null ? data.getAppUserDetail().getMemberType().toString() : null);
        dto.setParentType(data.getAppUserDetail().getParentType() != null ? data.getAppUserDetail().getParentType().toString() : null);
        dto.setParentCin(data.getAppUserDetail().getParentCin());
        dto.setParentBankAccount(data.getAppUserDetail().getParentBankAccount());
        dto.setOrders(data.getAppUserDetail().getOrders());

        Branch branch = data.getAppUserDetail().getBranchCode();
        BranchCodeResponse branchCodeResponse = new BranchCodeResponse();
        if (branch != null) {
            branchCodeResponse.setId(branch.getSecureId());
            branchCodeResponse.setName(branch.getName());
            branchCodeResponse.setCode(branch.getCode());
        }
        dto.setBranchCode(branchCodeResponse);
        dto.setPicName(data.getAppUserDetail().getPicName());

        TreeUserResponse treeUserResponse = new TreeUserResponse();

        // business
        List<Business> businesses = data.getBusinesses();
        dto.setBusinesses(treeUserResponse.convertListBusinesses(businesses));
//
        // expect
        List<ExpectCategoryList> expectCategories = treeUserResponse.convertExpectCategories(data.getUserHasExpects());
        dto.setExpectCategory(expectCategories);

        dto.setOrders(data.getAppUserDetail().getOrders());
        dto.setStatus(
                data.getAppUserDetail().getStatus().equals(StatusType.ACTIVATED)
                        ? data.getAppUserAttribute().getIsSuspended()
                        ? data.getAppUserAttribute().getIsDeleted()
                        ? "Deleted"
                        : "Suspended"
                        : "Active"
                        : "Inactive"
        );
        if (data.getAppUserAttribute().getIsSuspended()){
            dto.setSuspendedReason(data.getLog() != null && !data.getLog().isEmpty() ? data.getLog().stream()
                    .filter(l -> l.getStatus().equals(LogStatus.SUSPENDED))
                    .reduce((first, second) -> second)
                    .map(UserManagementLog::getMessage)
                    .orElse(null) : null);
        }
        if (data.getAppUserAttribute().getIsDeleted()){
            dto.setDeletedReason(data.getLog() != null && !data.getLog().isEmpty() ? data.getLog().stream()
                    .filter(l -> l.getStatus().equals(LogStatus.DELETED))
                    .reduce((first, second) -> second)
                    .map(UserManagementLog::getMessage)
                    .orElse(null) : null);
        }
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getAppUserDetail().getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getAppUserDetail().getUpdatedAt()));
    }
}

