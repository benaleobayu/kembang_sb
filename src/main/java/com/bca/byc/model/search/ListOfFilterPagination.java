package com.bca.byc.model.search;

import com.bca.byc.enums.AdminApprovalStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ListOfFilterPagination {
    private String keyword;
    private LocalDate startDate;
    private LocalDate endDate;
    private AdminApprovalStatus adminApprovalStatus;
    private String reportStatus;
    private String roleId;
    private Boolean status;
    private String stringStatus;
    private LocalDate postAt;
    private String location;
    private Boolean isSubscriber;

    public ListOfFilterPagination(String keyword) {
        this.keyword = keyword;
    }

    public ListOfFilterPagination(String keyword, LocalDate startDate, LocalDate endDate) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ListOfFilterPagination(String keyword, String roleId, Boolean status) {
        this.keyword = keyword;
        this.roleId = roleId;
        this.status = status;
    }

}
