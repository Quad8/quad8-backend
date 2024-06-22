package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
