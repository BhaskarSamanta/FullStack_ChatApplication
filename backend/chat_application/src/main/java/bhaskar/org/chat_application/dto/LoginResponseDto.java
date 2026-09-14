package bhaskar.org.chat_application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
    private String message;
    private String token;
    private String tokenType;
    private String refreshToken;
}
