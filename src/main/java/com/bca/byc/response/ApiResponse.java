package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiResponse {

    private Boolean success;
    private String message;

    private Object data;

    public ApiResponse(Boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
