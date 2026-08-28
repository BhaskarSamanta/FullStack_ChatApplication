package bhaskar.org.chat_application.repository;

import bhaskar.org.chat_application.entities.OtpVerification;
import bhaskar.org.chat_application.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByUser(User user);

    void deleteByUser(User user);
}
