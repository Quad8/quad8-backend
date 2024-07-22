package site.keydeuk.store.domain.customoption.dto.custom;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import site.keydeuk.store.entity.CustomOption;

import java.util.List;

@Getter@Setter
@Builder
public class CustomOptionDto {

    private String layout; //type

    private String appearanceTexture; //texture

    private String appearanceColor; //boardcolor

    private String baseKeyColor;

    private String keyboardSwitch; //switchtype

    private boolean hasPointKey; //hasPointKeyCap

    private String pointKeyType;

    private String pointSetColor;

    private String imgUrl;

    private Long price;

    public CustomOption toEntity(){
        return CustomOption.builder()
                .layout(this.layout)
                .appearanceTexture(this.appearanceTexture)
                .appearanceColor(this.appearanceColor)
                .baseKeyColor(this.baseKeyColor)
                .keyboardSwitch(this.keyboardSwitch)
                .hasPointKey(this.hasPointKey)
                .pointKeyType(this.pointKeyType)
                .pointSetColor(this.pointSetColor)
                .imgUrl(this.imgUrl)
                .price(this.price)
                .build();
    }
}
