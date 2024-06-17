package site.keydeuk.store.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
