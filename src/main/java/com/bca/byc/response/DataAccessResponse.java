package com.bca.byc.response;

import lombok.Data;

@Data
public class DataAccessResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn;

    public DataAccessResponse(String accessToken, String tokenType, long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    private String status;

    public DataAccessResponse(String accessToken, String tokenType, long expiresIn, String status) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.status = status;
    }
}
