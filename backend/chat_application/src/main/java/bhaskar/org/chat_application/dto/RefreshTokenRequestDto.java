package bhaskar.org.chat_application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {
    @NotBlank(message = "Refresh token can not be blank")
    private String refreshToken;
}
