package site.keydeuk.store.domain.community.dto.purchase;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    private int price;

    private Object individualColor;// productId로 조회
}
