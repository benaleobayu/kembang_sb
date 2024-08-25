package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class dataAccess {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
}
