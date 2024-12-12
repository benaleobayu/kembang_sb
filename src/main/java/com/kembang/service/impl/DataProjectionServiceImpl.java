package com.kembang.service.impl;

import com.kembang.entity.Product;
import com.kembang.model.dto.SavedLongAndStringValue;
import com.kembang.model.dto.SavedStringAndIntegerValue;
import com.kembang.repository.OrderHasProductRepository;
import com.kembang.repository.ProductRepository;
import com.kembang.service.DataProjectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DataProjectionServiceImpl implements DataProjectionService {

    private final ProductRepository productRepository;
    private final OrderHasProductRepository orderHasProductRepository;

    @Override
    public Map<String, Integer> listDataProductPriceByProductSecureId(List<String> idList) {
        List<SavedStringAndIntegerValue> queryList = productRepository.listDataProductPriceByProductSecureId(idList);
        return parserSavedStringAndInteger(queryList);
    }

    @Override
    public Map<Long, List<String>> listOrderHasProductSecureIdByOrderId(List<Long> idList) {
        List<SavedLongAndStringValue> queryList = productRepository.listOrderHasProductSecureIdByOrderId(idList);
        return parserSavedLongAndListString(queryList);
    }


    // -- helper --
    private Map<String, Integer> parserSavedStringAndInteger(List<SavedStringAndIntegerValue> queryList) {
        Map<String, Integer> listIdMap = new HashMap<>();
        for (SavedStringAndIntegerValue q : queryList) {
            if (!listIdMap.containsKey(q.getKey())) {
                listIdMap.put(q.getKey(), q.getValue());
            }
        }
        return listIdMap;
    }

    private Map<Long, List<String>> parserSavedLongAndListString(List<SavedLongAndStringValue> queryList) {
        Map<Long, List<String>> listIdMap = new HashMap<>();
        for (SavedLongAndStringValue q : queryList) {
            listIdMap
                    .computeIfAbsent(q.getKey(), k -> new ArrayList<>())
                    .add(q.getValue());
        }
        return listIdMap;
    }
}
