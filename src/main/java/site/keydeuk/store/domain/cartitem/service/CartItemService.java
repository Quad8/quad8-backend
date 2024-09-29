package site.keydeuk.store.domain.cartitem.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.cart.repository.CartRepository;
import site.keydeuk.store.domain.cartitem.dto.CustomUpdateRequestDto;
import site.keydeuk.store.domain.cartitem.dto.delete.DeleteCartItemRequestDto;
import site.keydeuk.store.domain.cartitem.dto.update.ProductUpdateRequestDto;
import site.keydeuk.store.domain.cartitem.repository.CartItemRepository;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.domain.customoption.service.CustomObjectService;
import site.keydeuk.store.domain.customoption.service.CustomService;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.entity.*;

import static site.keydeuk.store.common.response.ErrorCode.COMMON_INVALID_PARAMETER;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CustomService customService;
    private final ImageService imageService;

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
    public void updateProduct(Long cartItemId, ProductUpdateRequestDto dto){
        CartItemWithProduct cartItem = (CartItemWithProduct) cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        log.info("customId: {}",cartItem.getProduct().getId());
        int categoryId = cartItem.getProduct().getProductCategory().getId();

        if (categoryId == 1) {
            boolean flag = true;
            if (dto.getSwitchOptionId() == null){
                throw new CustomException("키보드 스위치 옵션 선택 필수입니다.", COMMON_INVALID_PARAMETER);
            }else {
                for (ProductSwitchOption switchOption : cartItem.getProduct().getSwitchOptions()){
                    if (switchOption.getId() == dto.getSwitchOptionId()){
                        flag = false;
                    }
                }
                if (flag){
                    throw new CustomException("존재하지 않는 스위치 옵션입니다.", COMMON_INVALID_PARAMETER);
                }
            }
        }

        cartItem.setCount(dto.getCount());
        if (dto.getSwitchOptionId() != null) cartItem.setOptionId(dto.getSwitchOptionId());
        cartItemRepository.save(cartItem);
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
                int customId = customItem.getCustomOption().getId();
                String url = customItem.getCustomOption().getImgUrl();

                cartItemRepository.deleteByIdAndCartId(cartItemId, cartItem.getCart().getId());
                cart.setTotalCount(cart.getTotalCount() - 1);
                /**
                 * cartitem 테이블만 삭제됨, custom table은 살아 있어 어떻게 처리할지
                 * */
                log.info("[cart] Delete cart by customKeyboard id :{}",customId);
                customService.deleteCustomKeyboad(customId);
                imageService.deleteImage(url);
            }
        }
    }

}
