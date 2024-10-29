package com.bca.byc.model;

import lombok.Data;

@Data
public class UpdateProfileAdminRequest {

    private String name;

    private String email;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

    private Boolean isVisible;

    private Boolean status;
}
