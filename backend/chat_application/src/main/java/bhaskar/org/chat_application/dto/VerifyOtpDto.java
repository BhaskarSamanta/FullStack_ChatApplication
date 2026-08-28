package bhaskar.org.chat_application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerifyOtpDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(
            regexp = "\\d{6}",
            message = "OTP must be a 6-digit number"
    )
    private String otp;
}
