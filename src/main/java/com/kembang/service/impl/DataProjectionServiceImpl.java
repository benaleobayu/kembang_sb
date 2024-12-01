package com.kembang.service.impl;

import com.kembang.entity.Product;
import com.kembang.model.dto.SavedStringAndIntegerValue;
import com.kembang.repository.ProductRepository;
import com.kembang.service.DataProjectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DataProjectionServiceImpl implements DataProjectionService {

    private final ProductRepository productRepository;

    @Override
    public Map<String, Integer> listDataProductPriceByProductSecureId(List<String> idList) {
        List<SavedStringAndIntegerValue> queryList = productRepository.listDataProductPriceByProductSecureId(idList);
        return parserSavedStringAndInteger(queryList);
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
}
