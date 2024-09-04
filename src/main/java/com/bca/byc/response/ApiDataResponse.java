package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiDataResponse<T> {

    private boolean success;
    private String message;
    private Object data;
}
