package site.keydeuk.store.domain.customoption.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.entity.CustomObject;

@RequiredArgsConstructor
@Service
public class CustomObjectService {

    private final CustomObjectRepository customObjectRepository;

    public void saveObjects(CustomObject customObject){
        customObjectRepository.save(customObject);
    }


}
