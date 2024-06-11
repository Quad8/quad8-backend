package site.keydeuk.store.domain.customoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CustomOption;
@Repository
public interface CustomRepository extends JpaRepository<CustomOption,Integer> {

}
