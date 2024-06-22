package site.keydeuk.store.domain.payment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentResponse(
        Long id,
        Long orderId,
        String paymentKey,
        String method,
        Long totalAmount,
        String status,
        LocalDateTime requestedAt,
        LocalDateTime approvedAt,
        String lastTransactionKey
) {

}
