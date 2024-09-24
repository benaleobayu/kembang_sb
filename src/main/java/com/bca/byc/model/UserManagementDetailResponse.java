package com.bca.byc.model;

import com.bca.byc.model.data.BusinessListResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserManagementDetailResponse {

    private Long id;
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
    private String branchCode;
    private String picName;

    private List<BusinessListResponse> businesses = new ArrayList<>();

    private List<ExpectCategoryList> expectCategory = new ArrayList<>();

    private Long orders;
    private String status;
    private String createdAt;
    private String updatedAt;

    // inner
    @Data
    public static class ExpectCategoryList {
        private Long id;
        private String name;
        private List<SubExpectCategoryList> subCategories = new ArrayList<>();
    }

    @Data
    public static class SubExpectCategoryList {
        private Long id;
        private String name;
    }

}
