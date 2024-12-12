package com.kembang.model;

public record ListOrderDTO(

        String orderId,

        String productId,

        String productName,

        Integer quantity,

        Integer totalPrice
) {
}
