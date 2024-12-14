package com.kembang.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderRouteCreateUpdateRequest {

    private Integer route;
    private String driverName;
    private LocalDate date;

}
