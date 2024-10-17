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
    private String label;

    public UserManagementFilterList(LocalDate startDate, LocalDate endDate, Long locationId, UserType segmentation, String label) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.locationId = locationId;
        this.segmentation = segmentation;
        this.label = label;
    }
}
