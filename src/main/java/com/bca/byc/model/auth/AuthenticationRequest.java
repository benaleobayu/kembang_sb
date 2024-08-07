package com.bca.byc.model.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRequest {
    // Getters and Setters
    private String email;
    private String password;

}
