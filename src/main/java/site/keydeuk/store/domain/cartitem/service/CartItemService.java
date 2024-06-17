package site.keydeuk.store.domain.cartitem.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.cart.repository.CartRepository;
import site.keydeuk.store.domain.cartitem.dto.CustomUpdateRequestDto;
import site.keydeuk.store.domain.cartitem.dto.delete.DeleteCartItemRequestDto;
import site.keydeuk.store.domain.cartitem.repository.CartItemRepository;
import site.keydeuk.store.domain.customoption.service.CustomService;
import site.keydeuk.store.entity.Cart;
import site.keydeuk.store.entity.CartItem;
import site.keydeuk.store.entity.CartItemWithCustom;
import site.keydeuk.store.entity.CartItemWithProduct;
@Slf4j
@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CustomService customService;

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

    @Transactional
    public void updateCustom(Long cartItemId, CustomUpdateRequestDto dto){
        CartItemWithCustom cartItem = (CartItemWithCustom) cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        log.info("customId: {}",cartItem.getCustomOption().getId());
        customService.updateCustomOption(cartItem.getCustomOption().getId(),dto);
    }

    @Transactional
    public void delete(DeleteCartItemRequestDto dto, Long userId){
        for (Long cartItemId : dto.getDeletedProducts()){
            CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
            Cart cart = cartRepository.findByUserId(userId);
            if (cartItem instanceof CartItemWithProduct){
                CartItemWithProduct productItem = (CartItemWithProduct) cartItem;
                cartItemRepository.deleteByIdAndCartId(cartItemId, cartItem.getCart().getId());
                cart.setTotalCount(cart.getTotalCount() - 1);
            }else if (cartItem instanceof CartItemWithCustom){
                CartItemWithCustom customItem = (CartItemWithCustom) cartItem;
                cartItemRepository.deleteByIdAndCartId(cartItemId, cartItem.getCart().getId());
                cart.setTotalCount(cart.getTotalCount() - 1);
                /**
                 * cartitem 테이블만 삭제됨, custom table은 살아 있어 어떻게 처리할지
                 * */
            }
        }
    }

}
