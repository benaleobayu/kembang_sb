package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiListResponse {

    private Boolean success;
    private String message;
    private Object data;
}
