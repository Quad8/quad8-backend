package site.keydeuk.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import site.keydeuk.store.common.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
public class Payment extends BaseTimeEntity {
    @Id
    Long id;
    String paymentKey;
    String method;
    Long totalAmount;
    String status;
    LocalDateTime requestedAt;
    LocalDateTime approvedAt;
    String lastTransactionKey;

}
