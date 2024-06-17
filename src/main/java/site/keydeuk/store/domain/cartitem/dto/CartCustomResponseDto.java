package site.keydeuk.store.domain.cartitem.dto;

import lombok.*;
import site.keydeuk.store.entity.CustomObject;
import site.keydeuk.store.entity.CustomOption;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartCustomResponseDto {

    private Long id;

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

    private String classification; //shop or custom

    public static CartCustomResponseDto fromEntity(Long id, CustomOption custom, CustomObject object){
        return CartCustomResponseDto.builder()
                .id(id)
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
                .classification("CUSTOM")
                .build();
    }
}
