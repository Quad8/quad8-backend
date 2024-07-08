package site.keydeuk.store.domain.payment.dto.request;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Long amount
) {
    public static PaymentConfirmRequest of(String paymentKey, String orderId, Long amount) {
        return new PaymentConfirmRequest(paymentKey, orderId, amount);
    }

    public static PaymentConfirmRequest from(PaymentRequest request) {
        return of(request.paymentKey(), request.paymentOrderId(), request.amount());
    }
}
