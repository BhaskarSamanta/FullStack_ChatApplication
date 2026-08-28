package bhaskar.org.chat_application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationOtp(String email, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Otp Verification from chat Application");
        message.setText(
                "Your email verification OTP is: " + otp +
                "\n\nThis Otp expires in 5 minutes"+
                "\n\nDo not share this OTP with anyone."
        );

        mailSender.send(message);
    }
}
