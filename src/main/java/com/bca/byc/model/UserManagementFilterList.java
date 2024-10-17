package com.bca.byc.model;

import com.bca.byc.enums.UserType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserManagementFilterList {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long locationId;
    private UserType segmentation;
    private Boolean isSenior;

    public UserManagementFilterList(LocalDate startDate, LocalDate endDate, Long locationId, UserType segmentation, Boolean isSenior) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.locationId = locationId;
        this.segmentation = segmentation;
        this.isSenior = isSenior;
    }
}
