package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private dataAccess data;
    private String status;

}
