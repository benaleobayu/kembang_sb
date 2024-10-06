package com.bca.byc.model;

import lombok.Data;

@Data
public class BlacklistKeywordDetailResponse {

    private String id;

    private String keyword;

    private Boolean status;
}
