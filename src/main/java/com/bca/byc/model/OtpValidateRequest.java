package com.bca.byc.model;

public record OtpValidateRequest(
        String email,
        String otp
) {
}
