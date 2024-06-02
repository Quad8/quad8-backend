package site.keydeuk.store.domain.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTestRepository extends JpaRepository<UserTestEntity, Integer> {

    Optional<UserTestEntity> findById(Integer id);


}
