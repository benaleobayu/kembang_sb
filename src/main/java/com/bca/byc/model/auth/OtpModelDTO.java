package com.bca.byc.model.auth;

import lombok.Data;

public class OtpModelDTO {

    @Data
    public static class OtpResend {
        private String email;
    }

    @Data
    public static class OtpRequest {
        private String email;
        private String otp;
    }
}
