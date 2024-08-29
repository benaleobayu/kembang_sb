package com.bca.byc.model;

import lombok.Data;

@Data
public class AdminDetailResponse {

    private Long id;
    private String name;
    private String role;
    private String email;
    private boolean status;
    private String createdAt;
    private String updatedAt;

    public String getRole() {
        if (role != null && role.contains("name=")) {
            return role.substring(role.indexOf("name=") + 5, role.length() - 1);
        }
        return role;
    }

}
