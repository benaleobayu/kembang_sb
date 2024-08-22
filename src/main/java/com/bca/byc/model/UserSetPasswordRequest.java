package com.bca.byc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSetPasswordRequest {

    @NotBlank(message = "New Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Confirm Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String confirmPassword;

    @JsonIgnore
    public boolean isSetPasswordMatch() {
        return password.equals(confirmPassword);
    }
}
