package bhaskar.org.chat_application.repository;

import bhaskar.org.chat_application.entities.RefreshToken;
import bhaskar.org.chat_application.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository <RefreshToken, Long> {
    List<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByUserAndRevokedFalse(User user);

    List<RefreshToken> findByRevokedFalse();
}
