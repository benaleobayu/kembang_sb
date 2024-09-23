package com.bca.byc.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApiWithAttributeResponse {

    private Boolean success;
    private String message;
    private Object data;
    private List<Map<String, List<?>>> attributes;

    public ApiWithAttributeResponse(Boolean success, String message, Object data, List<Map<String, List<?>>> attributes) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.attributes = attributes;
    }

    public ApiWithAttributeResponse(Boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiWithAttributeResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
