package bhaskar.org.chat_application.service;

import bhaskar.org.chat_application.dto.SignupDto;
import bhaskar.org.chat_application.dto.VerifyOtpDto;
import bhaskar.org.chat_application.entities.Role;
import bhaskar.org.chat_application.entities.User;
import bhaskar.org.chat_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final OtpService otpService;

    @Transactional
    public void signup(SignupDto signupDto){

        // check if user exists by email
        if(userRepository.existsByEmail(signupDto.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        if(userRepository.existsByUsername(signupDto.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(signupDto.getUsername())
                .email(signupDto.getEmail())
                .passwordHashed(
                        passwordEncoder.encode(signupDto.getPassword())
                )
                .role(Role.USER)
                .isVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        String otp = otpService.createOtp(savedUser);
        emailService.sendVerificationOtp(savedUser.getEmail(), otp);
    }

    @Transactional
    public boolean verifyEmail(VerifyOtpDto verifyOtpDto){
        User user = userRepository.findByEmail(verifyOtpDto.getEmail()).orElse(null);
        if(user == null || user.isVerified()){
            return false;
        }

        boolean otpIsValid = otpService.verifyOtp(
                user,
                verifyOtpDto.getOtp()
        );

        if(!otpIsValid){
            return false;
        }

        user.setVerified(true);
        userRepository.save(user);

        return true;
    }
}
