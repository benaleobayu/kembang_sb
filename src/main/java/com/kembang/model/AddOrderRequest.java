package com.kembang.model;

public record AddOrderRequest (

        String orderId,

        String productId,

        Integer quantity,

        String orderNote
){
}
