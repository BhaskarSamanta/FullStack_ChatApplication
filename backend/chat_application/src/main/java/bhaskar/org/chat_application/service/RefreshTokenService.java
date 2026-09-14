package bhaskar.org.chat_application.service;

import bhaskar.org.chat_application.entities.RefreshToken;
import bhaskar.org.chat_application.entities.User;
import bhaskar.org.chat_application.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String createRefreshToken(User user) {

        String refreshToken = generateRefreshToken();

        String tokenHash = hashToken(refreshToken);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .expiresAt(
                        LocalDateTime.now()
                                .plusSeconds(refreshExpiration / 1000)
                )
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    private String generateRefreshToken() {

        byte[] randomBytes = new byte[64];

        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private String hashToken(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            token.getBytes(StandardCharsets.UTF_8)
                    );

            return Base64.getEncoder()
                    .encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e
            );
        }
    }

    public RefreshToken validateRefreshToken(String token) {

        String tokenHash = hashToken(token);

        List<RefreshToken> refreshTokens =
                refreshTokenRepository.findByRevokedFalse();

        for (RefreshToken refreshToken : refreshTokens) {

            if (refreshToken.getTokenHash().equals(tokenHash)) {

                if (refreshToken.getExpiresAt()
                        .isBefore(LocalDateTime.now())) {

                    throw new RuntimeException(
                            "Refresh Token Expired"
                    );
                }

                return refreshToken;
            }
        }

        throw new RuntimeException(
                "Invalid refresh token"
        );
    }
}