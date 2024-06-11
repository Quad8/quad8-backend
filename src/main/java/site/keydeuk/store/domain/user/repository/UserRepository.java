package site.keydeuk.store.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    boolean existsByNickname(String nickname);
}
