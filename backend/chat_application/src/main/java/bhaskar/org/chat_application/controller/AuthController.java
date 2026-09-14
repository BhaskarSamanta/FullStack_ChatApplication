package bhaskar.org.chat_application.controller;

import bhaskar.org.chat_application.dto.*;
import bhaskar.org.chat_application.service.AuthService;
import bhaskar.org.chat_application.service.OtpVerificationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupDto signupDto){
        authService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerifyOtpDto verifyOtpDto){
        OtpVerificationStatus isVerified = authService.verifyEmail(verifyOtpDto);
        return switch (isVerified) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.OK).body(
                    "Email verified Successfully"
            );
            case OTP_EXPIRED -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Otp has expired please try with a new otp"
            );
            case INVALID_OTP -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Invalid OTP please try again"
            );
            case MAX_ATTEMPTS_EXCEEDED -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Maximum OTP attempts reached please try again with a new OTP."
            );
            case OTP_NOT_FOUND -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Otp not found please request a new otp"
            );
            default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Otp verification failed due to unexpected error"
            );
        };
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@Valid @RequestBody ResendOtpDto resendOtpDto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.resendOtp(resendOtpDto.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto){
        bhaskar.org.chat_application.dto.LoginResponseDto response = authService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto request){
        LoginResponseDto response = authService.refreshAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
