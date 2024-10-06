package com.bca.byc.model.attribute;

import lombok.Data;

@Data
public class TotalCountResponse {

    private Integer total = 0;

    public TotalCountResponse() {
    }

    public TotalCountResponse(Integer total) {
        if (total != null) {
            this.total = total;
        }
    }
}