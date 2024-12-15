package com.kembang.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataAccessResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn;

    public DataAccessResponse(String accessToken, String tokenType, long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    private String restrict_code;

}
