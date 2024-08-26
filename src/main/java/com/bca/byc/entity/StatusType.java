package com.bca.byc.entity;


public enum StatusType {
    OTP, // 0
    PENDING, // 1 // the beginning of status
    APPROVED, // 2 // if cis is correct
    REJECTED, // 3 // if cis is wrong
    VERIFIED, // 4 // if user is verified otp
    PRE_ACTIVATED, // 5 // if user ready set the password
    ACTIVATED; // 6 // if user has been fill onboarding
}
