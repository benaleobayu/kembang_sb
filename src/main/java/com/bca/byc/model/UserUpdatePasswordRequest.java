package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@Data
public class UserUpdatePasswordRequest {

    @NotBlank(message = "Old Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String oldPassword;

    @NotBlank(message = "New Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    @NotBlank(message = "Confirm Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String confirmPassword;

    public boolean isPasswordMatch(){
        return newPassword.equals(confirmPassword);
    }
}
