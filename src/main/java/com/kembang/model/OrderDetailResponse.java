package com.kembang.model;

import java.time.LocalDate;
import java.util.List;

public record OrderDetailResponse(

        String id,

        String forwardName,

        String forwardAddress,

        String customerId,

        String customerName,

        String customerPhone,

        String customerAddress,

        String customerLocation,

        List<ListOrderDTO> listOrders,

        String orderDate,

        String deliveryDate,

        String driverName,

        Integer route,

        Boolean isPaid,

        Boolean isActive

) {
}
