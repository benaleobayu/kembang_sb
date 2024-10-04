package com.bca.byc.model.apps;

import lombok.Data;

import java.util.List;

@Data
public class ExpectCategoryUserInfoResponse {

    private String expectCategoryId;

    private String otherExpect;

    private Items items;

    @Data
    public static class Items {
        private List<String> ids;
        private String otherExpectItem;
    }
}