package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.Setter;

@Setter
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

    @Override
    public void addCount(int count) {
        this.setCount(this.getCount() + count);
    }
}
