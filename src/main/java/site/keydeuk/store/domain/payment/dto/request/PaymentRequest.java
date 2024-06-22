package site.keydeuk.store.domain.payment.dto.request;

public record PaymentRequest (
        String paymentKey,
        String paymentOrderId,
        Long amount
){
}
