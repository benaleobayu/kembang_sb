package com.kembang.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderCreateUpdateRequest {

    private String customerId;

    private String forwardName;

    private String forwardAddress;

    private String description;

    private String shortNote;

    private LocalDate deliveryDate;

    private List<AddOrderRequest> orderProducts;

    private String route;

    private Integer orders;

    private Boolean isPaid;

    private Boolean isActive;
}
