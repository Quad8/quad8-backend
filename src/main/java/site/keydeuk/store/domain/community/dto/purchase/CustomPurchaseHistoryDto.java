package site.keydeuk.store.domain.community.dto.purchase;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import site.keydeuk.store.entity.CustomObject;
import site.keydeuk.store.entity.CustomOption;
import site.keydeuk.store.entity.OrderItem;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CustomPurchaseHistoryDto {

    private Long orderId;

    private LocalDateTime createdAt;

    private boolean isReviewed;

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

    private Long price;

    private Object individualColor;// productId로 조회

    public CustomPurchaseHistoryDto(OrderItem orderItem, CustomOption customOption, boolean isReviewed){
        this.orderId = orderItem.getOrder().getId();
        this.createdAt = orderItem.getOrder().getCreatedAt();
        this.type = customOption.getLayout();
        this.productId = customOption.getId();
        this.texture = customOption.getAppearanceTexture();
        this.boardColor = customOption.getAppearanceColor();
        this.baseKeyColor = customOption.getBaseKeyColor();
        this.switchType = customOption.getKeyboardSwitch();
        this.hasPointKeyCap = customOption.isHasPointKey();
        this.pointKeyType = customOption.getPointKeyType();
        this.pointSetColor = customOption.getPointSetColor();
        this.imgUrl = customOption.getImgUrl();
        this.price = customOption.getPrice();
        this.isReviewed = isReviewed;
    }
    public CustomPurchaseHistoryDto(OrderItem orderItem, CustomOption customOption, CustomObject object,boolean isReviewed){
        this.orderId = orderItem.getOrder().getId();
        this.createdAt = orderItem.getOrder().getCreatedAt();
        this.type = customOption.getLayout();
        this.productId = customOption.getId();
        this.texture = customOption.getAppearanceTexture();
        this.boardColor = customOption.getAppearanceColor();
        this.baseKeyColor = customOption.getBaseKeyColor();
        this.switchType = customOption.getKeyboardSwitch();
        this.hasPointKeyCap = customOption.isHasPointKey();
        this.pointKeyType = customOption.getPointKeyType();
        this.pointSetColor = customOption.getPointSetColor();
        this.imgUrl = customOption.getImgUrl();
        this.price = customOption.getPrice();
        this.isReviewed = isReviewed;
        this.individualColor = object.getObjects();
    }
}
