package site.keydeuk.store.domain.cartitem.dto;

import lombok.*;
import site.keydeuk.store.entity.CartItem;
import site.keydeuk.store.entity.Product;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponseDto {

    private Long id;

    private int prductId;

    private int price;

    private int count;

    private String thumbsnail;

    private String productTitle;

    private Long optionId;

    private String optionName;

    private String classification; //shop or custom

    public static CartProductResponseDto fromEntity(CartItem item, Product product){

        return CartProductResponseDto.builder()
                .id(item.getId())
                .prductId(product.getId())
                .price(product.getPrice())
                .count(item.getCount())
                .thumbsnail(product.getProductImgs().get(0).getImgUrl())
                .productTitle(product.getName())
                .optionId(item.getOptionId())
                .optionName(null)
                .classification("SHOP")
                .build();
    }

    public static CartProductResponseDto fromEntity(CartItem item, Product product, String optionName){

        return CartProductResponseDto.builder()
                .id(item.getId())
                .prductId(product.getId())
                .price(product.getPrice())
                .count(item.getCount())
                .thumbsnail(product.getProductImgs().get(0).getImgUrl())
                .productTitle(product.getName())
                .optionId(item.getOptionId())
                .optionName(optionName)
                .classification("SHOP")
                .build();
    }
}

