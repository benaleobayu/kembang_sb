package com.bca.byc.model.auth;

import com.bca.byc.entity.LogDevice;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class LoginRequest {
    private String email;
    private String password;
}