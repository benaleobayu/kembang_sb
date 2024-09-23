package com.bca.byc.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApiAttributeResponse {

    private Boolean success;
    private String message;
    private List<Map<String, List<?>>> attributes;


    public ApiAttributeResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiAttributeResponse(Boolean success, String message, List<Map<String, List<?>>> attributes) {
        this.success = success;
        this.message = message;
        this.attributes = attributes;
    }
}