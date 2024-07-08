package site.keydeuk.store.domain.payment.dto.request;

public record PaymentRequest (
        Long orderId,
        String paymentKey,
        String paymentOrderId,
        Long amount
){
}
