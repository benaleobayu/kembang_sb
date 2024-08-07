package com.bca.byc.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiListResponse {

    private boolean success;
    private String message;
    private Object data;

}
