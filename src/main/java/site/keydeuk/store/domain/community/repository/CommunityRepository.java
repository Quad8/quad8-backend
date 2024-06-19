package site.keydeuk.store.domain.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Community;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {

    Community findByIdAndUser_Id(Long id, Long userId);

}
