package site.keydeuk.store.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
