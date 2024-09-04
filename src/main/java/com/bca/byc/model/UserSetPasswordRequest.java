package com.bca.byc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
