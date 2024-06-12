package site.keydeuk.store.domain.customoption.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.customoption.dto.custom.CustomKeyboardRequestDto;
import site.keydeuk.store.entity.CustomOption;

@RequiredArgsConstructor
@Service
public class CustomService {

    public void saveCustomOption(CustomKeyboardRequestDto dto){
        //base64 -> s3저
    }

//    private CustomOption toEntity(CustomKeyboardRequestDto dto, String imgUrl) {
//        return CustomOption.builder()
//                .layout(dto.getType())
//                .appearanceTexture(dto.getTexture())
//                .appearanceColor(dto.getBoardColor())
//                .baseKeyColor(dto.getBaseKeyColor())
//                .keyboardSwitch(dto.getSwitchType())
//                .hasPointKey(dto.getHasPointKeyCap())
//                .pointKeyType(dto.getPointKeyType())
//                .pointSetColor(dto.getPointSetColor())
//                .imgUrl(imgUrl)
//                .price(dto.getPrice())
//                .build();
//    }
}
