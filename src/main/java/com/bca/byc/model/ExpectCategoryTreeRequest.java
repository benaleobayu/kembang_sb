package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class ExpectCategoryTreeRequest {

    private String expectCategoryId;

    private String otherExpect;

    private Items items;

    @Data
    public static class Items {
        private List<String> ids;
        private String otherExpectItem;
    }
}
