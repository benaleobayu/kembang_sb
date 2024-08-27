package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class dataAccess {
    private String accessToken;
    private String tokenType;
    private long expiresIn;

    public dataAccess(String accessToken, String tokenType, long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    private String status;

    public dataAccess(String accessToken, String tokenType, long expiresIn, String status) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.status = status;
    }
}
