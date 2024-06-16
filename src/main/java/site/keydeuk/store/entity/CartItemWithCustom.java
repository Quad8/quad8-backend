package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@DiscriminatorValue("CUSTOM_OPTION")
public class CartItemWithCustom extends CartItem{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="customOptionId")
    private CustomOption customOption;

    public static CartItemWithCustom createCartItem(Cart cart, CustomOption customOption, int count){
        CartItemWithCustom cartItem = new CartItemWithCustom();
        cartItem.setCart(cart);
        cartItem.setCustomOption(customOption);
        cartItem.setCount(count);
        return cartItem;
    }

    @Override
    public void addCount(int count) {
        this.setCount(this.getCount() + count);
    }
}
