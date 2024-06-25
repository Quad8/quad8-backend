package site.keydeuk.store.domain.customoption.dto.custom;

import lombok.*;
import site.keydeuk.store.domain.cartitem.dto.CartCustomResponseDto;
import site.keydeuk.store.entity.CustomObject;
import site.keydeuk.store.entity.CustomOption;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomDetailDto {

    private int productId;

    private String type; //type

    private String texture; //texture

    private String boardColor; //boardcolor

    private String baseKeyColor;

    private String switchType; //switchtype

    private boolean hasPointKeyCap; //hasPointKeyCap

    private String pointKeyType;

    private String pointSetColor;

    private String imgUrl;

    private int price;

    private Object individualColor;// productId로 조회

    public static CustomDetailDto fromEntity(CustomOption custom, CustomObject object){
        return CustomDetailDto.builder()
                .productId(custom.getId())
                .type(custom.getLayout())
                .texture(custom.getAppearanceTexture())
                .boardColor(custom.getAppearanceColor())
                .baseKeyColor(custom.getBaseKeyColor())
                .switchType(custom.getKeyboardSwitch())
                .hasPointKeyCap(custom.isHasPointKey())
                .pointKeyType(custom.getPointKeyType())
                .pointSetColor(custom.getPointSetColor())
                .imgUrl(custom.getImgUrl())
                .price(custom.getPrice())
                .individualColor(object.getObjects())
                .build();
    }

    public static CustomDetailDto fromEntity(CustomOption custom){
        return CustomDetailDto.builder()
                .productId(custom.getId())
                .type(custom.getLayout())
                .texture(custom.getAppearanceTexture())
                .boardColor(custom.getAppearanceColor())
                .baseKeyColor(custom.getBaseKeyColor())
                .switchType(custom.getKeyboardSwitch())
                .hasPointKeyCap(custom.isHasPointKey())
                .pointKeyType(custom.getPointKeyType())
                .pointSetColor(custom.getPointSetColor())
                .imgUrl(custom.getImgUrl())
                .price(custom.getPrice())
                .individualColor(null)
                .build();
    }
}
