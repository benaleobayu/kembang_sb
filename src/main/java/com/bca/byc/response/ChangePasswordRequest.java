package com.bca.byc.response;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

    // Getters and Setters (Lombok will handle this with @Data annotation)
}
