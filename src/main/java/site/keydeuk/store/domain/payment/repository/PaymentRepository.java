package site.keydeuk.store.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
}
