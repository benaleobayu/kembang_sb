package com.bca.byc.model.auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthenticationRequest {
    // Getters and Setters
    private String email;
    private String password;

}
