package site.keydeuk.store.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.OrderItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_UserIdAndProductId(Long userId, Integer productId);

    OrderItem findByOrderIdAndProductId(Long orderId, Integer productId);
}
