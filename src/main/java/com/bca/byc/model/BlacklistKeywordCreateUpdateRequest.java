package com.bca.byc.model;

import lombok.Data;

@Data
public class BlacklistKeywordCreateUpdateRequest {

    private String keyword;

    private Boolean status;

}
