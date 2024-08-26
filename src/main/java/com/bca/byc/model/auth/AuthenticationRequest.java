package com.bca.byc.model.auth;

import lombok.*;

@Data
@AllArgsConstructor
public class AuthenticationRequest {
    // Getters and Setters
    private String email;
    private String password;

}
