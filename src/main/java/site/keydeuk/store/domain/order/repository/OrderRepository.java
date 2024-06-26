package site.keydeuk.store.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.Order;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    Optional<Order> findByPaymentOrderId(String paymentOrderId);

    List<Order> findByUserIdAndStatusNot(Long userId, OrderStatus status);

    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
}
