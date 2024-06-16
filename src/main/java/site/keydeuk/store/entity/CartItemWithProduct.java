package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("PRODUCT")
public class CartItemWithProduct extends CartItem{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="productId")
    private Product product;

    public static CartItemWithProduct createCartItem(Cart cart, Product product, int count){
        CartItemWithProduct cartItem = new CartItemWithProduct();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCount(count);
        return cartItem;
    }
    public static CartItemWithProduct createCartItem(Cart cart, Product product, int count, Long optionId){
        CartItemWithProduct cartItem = new CartItemWithProduct();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCount(count);
        cartItem.setOptionId(optionId);
        return cartItem;
    }

    @Override
    public void addCount(int count) {
        this.setCount(this.getCount() + count);
    }
}
