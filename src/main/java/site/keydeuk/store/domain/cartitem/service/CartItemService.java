package site.keydeuk.store.domain.cartitem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.cartitem.repository.CartItemRepository;
import site.keydeuk.store.entity.CartItem;
import site.keydeuk.store.entity.CartItemWithCustom;
import site.keydeuk.store.entity.CartItemWithProduct;

@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemWithProduct findCartItemByCartIdAndProductId(Long cartId, Integer productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    public CartItemWithProduct findByCartIdAndProductIdAndOptionId(Long cartId,Integer productId, Long optionId){
        return cartItemRepository.findByCartIdAndProductIdAndOptionId(cartId,productId,optionId);
    }

    public CartItemWithCustom findCartItemByCartIdAndCustomOptionId(Long cartId, Integer customOptionId) {
        return cartItemRepository.findByCartIdAndCustomOptionId(cartId, customOptionId);
    }

    public void save(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

}
