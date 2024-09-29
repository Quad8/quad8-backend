package site.keydeuk.store.domain.customoption.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.cartitem.dto.CustomUpdateRequestDto;
import site.keydeuk.store.domain.customoption.dto.OptionProductsResponseDto;
import site.keydeuk.store.domain.customoption.dto.custom.CustomKeyboardRequestDto;
import site.keydeuk.store.domain.customoption.dto.custom.CustomOptionDto;
import site.keydeuk.store.domain.customoption.dto.custom.ObjectDto;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.entity.CustomObject;
import site.keydeuk.store.entity.CustomOption;

import java.util.Base64;
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomService {

    private final ImageService imageService;

    private final CustomRepository customRepository;

    private final CustomObjectRepository customObjectRepository;

    private final ProductRepository productRepository;


    /** 커스텀 키보드 옵션 저장 */
    @Transactional
    public Integer saveCustomOption(CustomKeyboardRequestDto dto){
        //1. base64 -> s3저장
        String base64Image = dto.getImgBase64();
        String base64Data = base64Image.replaceAll("^data:image/[a-zA-Z]+;base64,", "");

        byte[] imageByte = Base64.getDecoder().decode(base64Data);
        String objectUrl = imageService.uploadBase64ToImage(imageByte);

        //2. custom_option DB 저장
        CustomOptionDto customOptionDto = CustomOptionDto.builder()
                .layout(dto.getType())
                .appearanceTexture(dto.getTexture())
                .appearanceColor(dto.getBoardColor())
                .baseKeyColor(dto.getBaseKeyColor())
                .keyboardSwitch(dto.getSwitchType())
                .hasPointKey(dto.getHasPointKeyCap())
                .pointKeyType(dto.getPointKeyType())
                .pointSetColor(dto.getPointSetColor())
                .imgUrl(objectUrl)
                .price(dto.getPrice())
                .build();
        CustomOption customOption = customRepository.save(customOptionDto.toEntity());

        // 3. color option DB저장 (포인트키캡 선택 : true 시)
         if (dto.getHasPointKeyCap()){
             ObjectDto objectDto = new ObjectDto(dto.getIndividualColor());
             customObjectRepository.save(objectDto.toEntity(customOption.getId()));
         }
         return customOption.getId();
    }

    /** 커스텀 키보드 옵션 수정 */
    @Transactional
    public void updateCustomOption(int customId ,CustomUpdateRequestDto dto){
        //1. base64 -> s3저장
        String base64Image = dto.getImgBase64();
        String base64Data = base64Image.replaceAll("^data:image/[a-zA-Z]+;base64,", "");

        byte[] imageByte = Base64.getDecoder().decode(base64Data);
        String objectUrl = imageService.uploadBase64ToImage(imageByte);

        //2. findbyid
        CustomOption customOption = customRepository.findById(customId).orElseThrow(EntityNotFoundException::new);
        boolean previousHasPointKey = customOption.isHasPointKey();

        log.info("dto.getBoardColor(): {}",dto.getBoardColor());

        //3. custom_option DB 수정
        customOption.setLayout(dto.getType());
        customOption.setAppearanceTexture(dto.getTexture());
        customOption.setAppearanceColor(dto.getBoardColor());
        customOption.setBaseKeyColor(dto.getBaseKeyColor());
        customOption.setKeyboardSwitch(dto.getSwitchType());
        customOption.setHasPointKey(dto.getHasPointKeyCap());
        customOption.setPointKeyType(dto.getPointKeyType());
        customOption.setPointSetColor(dto.getPointSetColor());
        customOption.setImgUrl(objectUrl);
        customOption.setPrice(dto.getPrice());

        //4. customObject DB 수정
        customRepository.save(customOption);

        // 3. color option DB저장
        if (previousHasPointKey) { //이전 포인트키캡 선택 : true 시
            CustomObject customObject = customObjectRepository.findById(customId);
            if (dto.getHasPointKeyCap()){
                customObject.setObjects(dto.getIndividualColor());
                customObjectRepository.save(customObject);
            }else {
                customObjectRepository.deleteById(customId);
            }
        }else { //이전 포인트키캡 선택 : false 시
            if (dto.getHasPointKeyCap()){
                ObjectDto objectDto = new ObjectDto(dto.getIndividualColor());
                customObjectRepository.save(objectDto.toEntity(customOption.getId()));
            }
        }

    }

    /** 커스텀 키보드 DB 삭제 */
    @Transactional
    public void deleteCustomKeyboad(int customId){
        customRepository.deleteById(customId);
        customObjectRepository.deleteById(customId);
        log.info("[Custom] Delete 커스텀 키보드 Id:{}",customId);
    }

    public boolean existBycustomId(Integer id){
        boolean flag = false;
        if (customRepository.findById(id).isPresent()) flag = true;
        return flag;
    }



}
