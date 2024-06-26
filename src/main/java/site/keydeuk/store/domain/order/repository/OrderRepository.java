package site.keydeuk.store.domain.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.keydeuk.store.entity.Order;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    Optional<Order> findByPaymentOrderId(String paymentOrderId);

    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status <> :status AND o.createdAt BETWEEN :startDate AND :endDate")
    Page<Order> findByUserIdAndStatusNotAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
}
