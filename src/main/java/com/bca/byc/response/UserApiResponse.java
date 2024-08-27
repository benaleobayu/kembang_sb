package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserApiResponse {
    private boolean success;
    private String message;
    private DataAccess data;

}



