package com.kembang.service;

import com.kembang.entity.Product;

import java.util.List;
import java.util.Map;

public interface DataProjectionService {

    // product --
    Map<String, Integer> listDataProductPriceByProductSecureId(List<String> productIdList);

    // order has product --
    Map<Long, List<String>> listOrderHasProductSecureIdByOrderId(List<Long> idList);
}
