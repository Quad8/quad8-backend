package site.keydeuk.store.domain.cart.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.cart.repository.CartRepository;
import site.keydeuk.store.domain.cartitem.dto.CartItemReqeustDto;
import site.keydeuk.store.domain.cartitem.repository.CartItemRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.Cart;
import site.keydeuk.store.entity.CartItem;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.User;

@RequiredArgsConstructor
@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public Long addCart(CartItemReqeustDto dto, Long userId){
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItem savedOCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(),dto.getProductId());

        if(savedOCartItem !=null){
            savedOCartItem.addCount(dto.getCount());
            cart.setTotalCount(cart.getTotalCount()+1);
            return savedOCartItem.getId();
        }else {
            CartItem cartItem = CartItem.createCartItem(cart,product,dto.getCount());
            cartItemRepository.save(cartItem);
            cart.setTotalCount(cart.getTotalCount()+1);
            return cartItem.getId();
        }

    }
}
