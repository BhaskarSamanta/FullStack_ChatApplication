package bhaskar.org.chat_application.controller;

import bhaskar.org.chat_application.dto.SignupDto;
import bhaskar.org.chat_application.dto.VerifyOtpDto;
import bhaskar.org.chat_application.service.AuthService;
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
        boolean isVerified = authService.verifyEmail(verifyOtpDto);
        if(!isVerified){
            return ResponseEntity.badRequest().body("Invalid OTP or email already verified");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Email verified successfully");
    }

}
