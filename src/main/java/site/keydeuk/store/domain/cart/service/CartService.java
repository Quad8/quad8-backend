package site.keydeuk.store.domain.cart.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.cart.repository.CartRepository;
import site.keydeuk.store.domain.cartitem.dto.CartItemReqeustDto;
import site.keydeuk.store.domain.cartitem.service.CartItemService;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.*;

import static site.keydeuk.store.common.response.ErrorCode.COMMON_INVALID_PARAMETER;

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

    /** 상품 장바구니 담기*/
    public Long addProductToCart(CartItemReqeustDto dto, Long userId){

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        int categoryId = product.getProductCategory().getId();

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        if (categoryId == 1) {
            boolean flag = true;
            if (dto.getSwitchOptionId() == null){
                throw new CustomException("키보드 스위치 옵션 선택 필수입니다.", COMMON_INVALID_PARAMETER);
            }else {
                for (ProductSwitchOption switchOption : product.getSwitchOptions()){
                    if (switchOption.getId() == dto.getSwitchOptionId()){
                        flag = false;
                    }
                }
                if (flag){
                    throw new CustomException("존재하지 않는 스위치 옵션입니다.", COMMON_INVALID_PARAMETER);
                }
            }
        }

        CartItemWithProduct savedOCartItem;

        if (categoryId == 1){
            savedOCartItem = cartItemService.findByCartIdAndProductIdAndOptionId(cart.getId(),dto.getProductId(),dto.getSwitchOptionId());
            if(savedOCartItem !=null){
                savedOCartItem.addCount(dto.getCount());
                cart.setTotalCount(cart.getTotalCount()+1);
                return savedOCartItem.getId();
            }else {
                CartItemWithProduct cartItem = CartItemWithProduct.createCartItem(cart,product,dto.getCount(),dto.getSwitchOptionId());
                cartItemService.save(cartItem);
                cart.setTotalCount(cart.getTotalCount()+1);
                return cartItem.getId();
            }

        }else {
            savedOCartItem = cartItemService.findCartItemByCartIdAndProductId(cart.getId(),dto.getProductId());
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
    }

    /* 커스텀 키보드 장바구니 담기**/
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
