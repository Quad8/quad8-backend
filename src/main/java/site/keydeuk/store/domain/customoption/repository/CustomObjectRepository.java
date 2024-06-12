package site.keydeuk.store.domain.customoption.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CustomObject;

import java.util.List;

@Repository
public interface CustomObjectRepository extends MongoRepository<CustomObject, Integer> {
    List<CustomObject> findById(int id);
}
