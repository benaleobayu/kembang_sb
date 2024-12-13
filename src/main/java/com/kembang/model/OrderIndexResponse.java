package com.kembang.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderIndexResponse extends ModelBaseDTOResponse<Integer> {

    private String forwardName;

    private String forwardAddress;

    private String customerName;

    private String customerAddress;

    private String customerPhone;

    private String customerLocation;

    private List<ListOrderDTO> listOrder;

    private String deliveryDate;

    private String driverName;

    private Integer route;

    private Boolean isPaid;

    private Boolean isActive;

    private String createdAt;

}
