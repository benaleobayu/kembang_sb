package com.bca.byc.model;

import com.bca.byc.model.apps.ExpectCategoryList;
import com.bca.byc.model.data.BusinessListResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserManagementDetailResponse extends AdminModelBaseDTOResponse<Long> {

    private String name;
    private String birthDate;
    private String phone;
    private String email;
    private String memberCin;
    private String memberCardNumber;
    private String type;
    private String memberType;
    private String parentCin;
    private String parentBankAccount;
    private String parentType;
    private BranchCodeResponse branchCode;
    private String picName;

    private List<BusinessListResponse> businesses = new ArrayList<>();
    private List<ExpectCategoryList> expectCategory = new ArrayList<>();

    private Integer orders;
    private String status;
    private String suspendedReason;
    private String deletedReason;

}

