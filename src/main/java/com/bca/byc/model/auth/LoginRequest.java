package com.bca.byc.model.auth;

public record LoginRequest(String email, String password) {

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
