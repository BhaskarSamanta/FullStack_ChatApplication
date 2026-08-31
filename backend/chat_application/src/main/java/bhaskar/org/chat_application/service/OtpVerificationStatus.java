package bhaskar.org.chat_application.service;

public enum OtpVerificationStatus {
    SUCCESS,
    OTP_NOT_FOUND,
    OTP_EXPIRED,
    MAX_ATTEMPTS_EXCEEDED,
    INVALID_OTP,
}
