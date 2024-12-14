package com.kembang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRouteIndexResponse extends ModelBaseDTOResponse<Integer> {

    private LocalDate date;
    private Integer route;
    private String driverName;
    private BigDecimal totalDistance;
    private Integer totalCost;
    private Integer totalRemainingCost;

}
