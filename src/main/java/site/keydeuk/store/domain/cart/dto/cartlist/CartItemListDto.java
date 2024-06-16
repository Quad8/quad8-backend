package site.keydeuk.store.domain.cart.dto.cartlist;

import lombok.*;
import site.keydeuk.store.entity.Cart;
import site.keydeuk.store.entity.CartItem;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemListDto {

    private int totalCount;

    private Long cartId;

    private List<CartItem> cartItems;

    public static CartItemListDto fromEntity(Cart cart) {
        return CartItemListDto.builder()
                .totalCount(cart.getTotalCount())
                .cartId(cart.getId())
                .cartItems(cart.getCartItems())
                .build();
    }
}
