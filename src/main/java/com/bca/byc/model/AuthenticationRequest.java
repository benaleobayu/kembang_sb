package com.bca.byc.model;

import lombok.Data;

@Data
public class AuthenticationRequest {
    // Getters and Setters
    private String email;
    private String password;

}
