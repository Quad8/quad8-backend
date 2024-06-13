package site.keydeuk.store.domain.customoption.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.customoption.dto.custom.CustomKeyboardRequestDto;
import site.keydeuk.store.domain.customoption.dto.custom.CustomOptionDto;
import site.keydeuk.store.domain.customoption.dto.custom.ObjectDto;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.entity.CustomOption;

import java.util.Base64;

@RequiredArgsConstructor
@Service
public class CustomService {

    private final ImageService imageService;

    private final CustomRepository customRepository;

    private final CustomObjectRepository customObjectRepository;

    public void saveCustomOption(CustomKeyboardRequestDto dto){
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

        //3. color option DB저장
        ObjectDto objectDto = new ObjectDto(dto.getIndividualColor());
        customObjectRepository.save(objectDto.toEntity(customOption.getId()));

    }

}
