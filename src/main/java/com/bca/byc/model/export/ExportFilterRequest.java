package com.bca.byc.model.export;

import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ExportFilterRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private AdminApprovalStatus status;
    private Long locationId;
    private UserType segmentation;
    private Boolean isSenior;

    public ExportFilterRequest(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ExportFilterRequest(AdminApprovalStatus status) {
        this.status = status;
    }

    public ExportFilterRequest(Long locationId) {
        this.locationId = locationId;
    }

    public ExportFilterRequest(LocalDate startDate, LocalDate endDate, AdminApprovalStatus status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public ExportFilterRequest(LocalDate startDate, LocalDate endDate, Long locationId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.locationId = locationId;
    }

    public ExportFilterRequest(LocalDate startDate, LocalDate endDate, UserType segmentation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.segmentation = segmentation;
    }

    public ExportFilterRequest(LocalDate startDate, LocalDate endDate, Long locationId, UserType segmentation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.locationId = locationId;
        this.segmentation = segmentation;
    }

    public ExportFilterRequest(LocalDate startDate, LocalDate endDate, Long locationId, UserType segmentation, Boolean isSenior) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.locationId = locationId;
        this.segmentation = segmentation;
        this.isSenior = isSenior;
    }
}