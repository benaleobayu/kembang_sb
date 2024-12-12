package com.kembang.model;

public record OtpValidateRequest(
        String email,
        String otp
) {
}
