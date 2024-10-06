package com.bca.byc.converter.parsing;

import com.bca.byc.entity.*;
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
        dto.setBranchCode(data.getAppUserDetail().getBranchCode() != null ? data.getAppUserDetail().getBranchCode().getCode() : null);
        dto.setName(data.getAppUserDetail().getName());
        dto.setBirthDate(data.getAppUserDetail().getMemberBirthdate() != null ?
                Formatter.formatLocalDate(data.getAppUserDetail().getMemberBirthdate()) : null);
        dto.setEmail(data.getEmail().toLowerCase());
        dto.setPhone(data.getAppUserDetail().getPhone());
        dto.setMemberCin(data.getAppUserDetail().getMemberCin() != null ?
                data.getAppUserDetail().getMemberCin() : null);
        dto.setCreatedAt(data.getAppUserDetail().getCreatedAt() != null ? Formatter.formatLocalDateTime(data.getAppUserDetail().getCreatedAt()) : null);
        dto.setUpdatedAt(data.getAppUserDetail().getUpdatedAt() != null ? Formatter.formatLocalDateTime(data.getAppUserDetail().getUpdatedAt()) : null);
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
        dto.setType(data.getAppUserDetail().getType().toString());
        dto.setMemberType(data.getAppUserDetail().getMemberType());
        dto.setParentCin(data.getAppUserDetail().getParentCin());
        dto.setParentBankAccount(data.getAppUserDetail().getParentBankAccount());
        dto.setOrders(data.getAppUserDetail().getOrders());

        Branch branch = data.getAppUserDetail().getBranchCode();
        BranchCodeResponse branchCodeResponse = new BranchCodeResponse();
        if (branch != null) {
            branchCodeResponse.setId(branch.getSecureId());
            branchCodeResponse.setName(branch.getName());
        }
        dto.setBranchCode(branchCodeResponse);
        dto.setPicName(data.getAppUserDetail().getPicName());

        TreeUserResponse treeUserResponse = new TreeUserResponse();

        // business
        List<Business> businesses = data.getBusinesses();
        dto.setBusinesses(treeUserResponse.convertListBusinesses(businesses));

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
            dto.setSuspendedReason(data.getLog().stream().findFirst().get().getMessage());
        }
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getAppUserDetail().getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getAppUserDetail().getUpdatedAt()));
    }
}

