package com.bca.byc.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
