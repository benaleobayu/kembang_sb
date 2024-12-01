package com.kembang.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataOrderResponse {

    private String orderId;

    private String productId;

    private String productName;

    private Integer quantity;

    private Integer totalPrice;


}
