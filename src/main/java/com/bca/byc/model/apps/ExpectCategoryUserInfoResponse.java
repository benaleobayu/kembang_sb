package com.bca.byc.model.apps;

import lombok.Data;

import java.util.List;

@Data
public class ExpectCategoryUserInfoResponse {
    private String categoryId;
    private String otherExpect;
    private ExpectItemUserInfoResponse items; //<item>

    @Data
    public static class ExpectItemUserInfoResponse {
        private List<String> ids;
        private String otherExpectItem;
    }
}