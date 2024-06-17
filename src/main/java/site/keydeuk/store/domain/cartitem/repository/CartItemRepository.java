package site.keydeuk.store.domain.cartitem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CartItem;
import site.keydeuk.store.entity.CartItemWithCustom;
import site.keydeuk.store.entity.CartItemWithProduct;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItemWithProduct findByCartIdAndProductId(Long cartId, Integer productId);
    CartItemWithProduct findByCartIdAndProductIdAndOptionId(Long cartId, Integer productId,Long OptionId);
    CartItemWithCustom findByCartIdAndCustomOptionId(Long cartId, Integer customOptionId);
    @Transactional
    void deleteByIdAndCartId(Long cartItemId, Long cartId);

}
