package site.keydeuk.store.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import site.keydeuk.store.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByOrderIdAndId(Long orderId, Long paymentId);

    Optional<Payment> findByPaymentKey(String paymentKey);
}
