package com.kembang.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderIndexProjection {

    private Long id;

    private String secureId;

    private String forwardName;

    private String forwardAddress;

    private String customerId;

    private String customerName;

    private String customerAddress;

    private String customerPhone;

    private String customerLocation;

    private LocalDate deliveryDate;

    private String driverName;

    private Integer route;

    private Boolean isPaid;

    private Boolean isActive;

    private LocalDateTime createdAt;

}
