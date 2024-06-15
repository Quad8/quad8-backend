package site.keydeuk.store.domain.cart.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.cart.repository.CartRepository;
import site.keydeuk.store.domain.cartitem.dto.CartItemReqeustDto;
import site.keydeuk.store.domain.cartitem.service.CartItemService;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.*;
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CustomRepository customRepository;

    private final CartItemService cartItemService;

    public Long addProductToCart(CartItemReqeustDto dto, Long userId){

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItemWithProduct savedOCartItem = cartItemService.findCartItemByCartIdAndProductId(cart.getId(),dto.getProductId());

        if(savedOCartItem !=null){
            savedOCartItem.addCount(dto.getCount());
            cart.setTotalCount(cart.getTotalCount()+1);
            return savedOCartItem.getId();
        }else {
            CartItemWithProduct cartItem = CartItemWithProduct.createCartItem(cart,product,dto.getCount());
            cartItemService.save(cartItem);
            cart.setTotalCount(cart.getTotalCount()+1);
            return cartItem.getId();
        }

    }

    public Long addCustomToCart(CartItemReqeustDto dto, Long userId){
        CustomOption custom = customRepository.findById(dto.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItemWithCustom savedOCartItem = cartItemService.findCartItemByCartIdAndCustomOptionId(cart.getId(),dto.getProductId());

        if(savedOCartItem !=null){
            savedOCartItem.addCount(dto.getCount());
            cart.setTotalCount(cart.getTotalCount()+1);
            return savedOCartItem.getId();
        }else {
            CartItemWithCustom cartItem = CartItemWithCustom.createCartItem(cart,custom,dto.getCount());
            cartItemService.save(cartItem);
            cart.setTotalCount(cart.getTotalCount()+1);
            return cartItem.getId();
        }
    }
}
