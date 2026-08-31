package bhaskar.org.chat_application.service;

import bhaskar.org.chat_application.dto.SignupDto;
import bhaskar.org.chat_application.dto.VerifyOtpDto;
import bhaskar.org.chat_application.entities.OtpVerification;
import bhaskar.org.chat_application.entities.Role;
import bhaskar.org.chat_application.entities.User;
import bhaskar.org.chat_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    public String resendOtp(@NotBlank @Email String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null || user.isVerified()){
            return "Email not found or already verified";
        }

        String otp = otpService.createOtp(user);

        emailService.sendVerificationOtp(user.getEmail(), otp);
        return "new otp sent";
    }

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
    public OtpVerificationStatus verifyEmail(VerifyOtpDto verifyOtpDto){
        User user = userRepository
                .findByEmail(verifyOtpDto.getEmail())
                .orElse(null);
        
        if(user == null || user.isVerified()){
            return OtpVerificationStatus.OTP_NOT_FOUND;
        }
        
        OtpVerificationStatus result = otpService.verifyOtp(user, verifyOtpDto.getOtp());
        if(result != OtpVerificationStatus.SUCCESS){
            return result;
        }
        
        user.setVerified(true);
        userRepository.save(user);
        
        return result;
    }
}
