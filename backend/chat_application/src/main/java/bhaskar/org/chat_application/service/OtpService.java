package bhaskar.org.chat_application.service;

import bhaskar.org.chat_application.entities.OtpVerification;
import bhaskar.org.chat_application.entities.User;
import bhaskar.org.chat_application.repository.OtpVerificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int MAX_ATTEMPTS = 5;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String createOtp(User user){

        otpVerificationRepository.deleteByUser(user);

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1000000)
        );

        OtpVerification otpVerification = OtpVerification.builder()
                .user(user)
                .otpHash(passwordEncoder.encode(otp))
                .attempts(0)
                .build();

        otpVerificationRepository.save(otpVerification);

        return otp;

    }

    @Transactional
    public boolean verifyOtp(User user, String enteredOtp){
        OtpVerification otpVerification = otpVerificationRepository
                .findByUser(user)
                .orElse(null);

        if(otpVerification == null){
            return false;
        }

        if(LocalDateTime.now().isAfter(otpVerification.getExpiresAt())){
            otpVerificationRepository.delete(otpVerification);
            return false;
        }

        if(otpVerification.getAttempts() >= MAX_ATTEMPTS){
            otpVerificationRepository.delete(otpVerification);
            return false;
        }

        boolean otpMatches = passwordEncoder.matches(enteredOtp,otpVerification.getOtpHash());

        if(!otpMatches){
            otpVerification.setAttempts(otpVerification.getAttempts() + 1);

            if(otpVerification.getAttempts() >= MAX_ATTEMPTS){
                otpVerificationRepository.delete(otpVerification);
            }
            return false;
        }

        otpVerificationRepository.delete(otpVerification);
        return true;

    }


}
