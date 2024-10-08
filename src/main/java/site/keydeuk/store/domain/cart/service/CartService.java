package site.keydeuk.store.domain.cart.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.cart.dto.cartlist.CartByUserResponseDto;
import site.keydeuk.store.domain.cart.dto.cartlist.CartItemListDto;
import site.keydeuk.store.domain.cart.repository.CartRepository;
import site.keydeuk.store.domain.cartitem.dto.CartCustomResponseDto;
import site.keydeuk.store.domain.cartitem.dto.CartItemReqeustDto;
import site.keydeuk.store.domain.cartitem.dto.CartProductResponseDto;
import site.keydeuk.store.domain.cartitem.repository.CartItemRepository;
import site.keydeuk.store.domain.cartitem.service.CartItemService;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.productswitchoption.repository.ProductSwitchOptionRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static site.keydeuk.store.common.response.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CustomRepository customRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomObjectRepository customObjectRepository;
    private final ProductSwitchOptionRepository productSwitchOptionRepository;

    private final CartItemService cartItemService;


    /** 상품 장바구니 담기*/
    @Transactional
    public Long addProductToCart(CartItemReqeustDto dto, Long userId){

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        int categoryId = product.getProductCategory().getId();

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Cart cart = createCart(user.getId());

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
                return savedOCartItem.getId();
            }else {
                CartItemWithProduct cartItem = CartItemWithProduct.createCartItem(cart,product,dto.getCount());
                cartItemService.save(cartItem);
                cart.setTotalCount(cart.getTotalCount()+1);
                return cartItem.getId();
            }
        }
    }

    /** 커스텀 키보드 장바구니 담기**/
    @Transactional
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

    /** user 장바구니 조회*/
    @Transactional(readOnly = true)
    public CartByUserResponseDto getCartByUserId(Long userId){
        log.info("userid: {}",userId);
        CartItemListDto dto = CartItemListDto.fromEntity(cartRepository.findByUserId(userId));

        List<CartProductResponseDto> cartProducts = new ArrayList<>();
        List<CartCustomResponseDto> cartCustoms = new ArrayList<>();

        for (CartItem item : dto.getCartItems()){
            CartItem cartItem = cartItemRepository.findById(item.getId()).orElseThrow(EntityNotFoundException::new);
            if (cartItem instanceof CartItemWithCustom) {
                CartItemWithCustom customItem = (CartItemWithCustom) cartItem;
                CustomObject object = customObjectRepository.findById(customItem.getCustomOption().getId())
                        .orElse(null);
                CartCustomResponseDto cartCustomDto;
                if (object == null){
                    cartCustomDto = CartCustomResponseDto.fromEntity(customItem.getId(),customItem.getCustomOption());
                }else {
                    cartCustomDto = CartCustomResponseDto.fromEntity(customItem.getId(),customItem.getCustomOption(),object);
                }
                cartCustoms.add(cartCustomDto);

            } else if (cartItem instanceof CartItemWithProduct) {
                CartItemWithProduct productItem = (CartItemWithProduct) cartItem;
                CartProductResponseDto cartProductDto;
                if (productItem.getProduct().getProductCategory().getId()==1){
                    ProductSwitchOption option = productSwitchOptionRepository.findById(cartItem.getOptionId()).orElseThrow(EntityNotFoundException::new);
                    cartProductDto = CartProductResponseDto.fromEntity(item,productItem.getProduct(),option.getOptionName());
                }else {
                    cartProductDto = CartProductResponseDto.fromEntity(item,productItem.getProduct());
                }
                cartProducts.add(cartProductDto);
            }
        }
        Cart cart = cartRepository.findByUserId(userId);
        return new CartByUserResponseDto(cart.getTotalCount(), cartCustoms,cartProducts);
    }

    /** Cart 생성하기 */
    @Transactional
    public Cart createCart(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }
        return cart;
    }

}
