package site.keydeuk.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    Long id;
    String paymentKey;
    String method;
    Long totalAmount;
    String status;
    LocalDateTime requestedAt;
    LocalDateTime approvedAt;
    String lastTransactionKey;
    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

}
