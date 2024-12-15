package com.kembang.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRouteIndexResponse extends ModelBaseDTOResponse<Integer> {

    private String date;
    private Integer route;
    private String driverName;
    private BigDecimal totalDistance = BigDecimal.valueOf(0);
    private Integer totalCost = 0;
    private Integer totalRemainingCost = 0;

}
