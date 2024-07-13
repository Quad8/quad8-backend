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

    private int productId;

    private int price;

    private int count;

    private String thumbsnail;

    private String productTitle;

    private Long optionId;

    private String optionName;

    private String category;

    private String classification; //shop or custom

    public static CartProductResponseDto fromEntity(CartItem item, Product product){
        String category;
        if (product.getProductCategory().getId() > 3) {
            category = "etc";
        } else {
            category = product.getProductCategory().getName();
        }
        return CartProductResponseDto.builder()
                .id(item.getId())
                .productId(product.getId())
                .price(product.getPrice())
                .count(item.getCount())
                .thumbsnail(product.getProductImgs().get(0).getImgUrl())
                .productTitle(product.getName())
                .optionId(item.getOptionId())
                .optionName(null)
                .category(category)
                .classification("SHOP")
                .build();
    }

    public static CartProductResponseDto fromEntity(CartItem item, Product product, String optionName){
        String category;
        if (product.getProductCategory().getId() > 3) {
            category = "etc";
        } else {
            category = product.getProductCategory().getName();
        }
        return CartProductResponseDto.builder()
                .id(item.getId())
                .productId(product.getId())
                .price(product.getPrice())
                .count(item.getCount())
                .thumbsnail(product.getProductImgs().get(0).getImgUrl())
                .productTitle(product.getName())
                .optionId(item.getOptionId())
                .optionName(optionName)
                .category(category)
                .classification("SHOP")
                .build();
    }
}

