package site.keydeuk.store.domain.order.dto.request;

public record OrderUpdateRequest(
        String paymentOrderId,
        Long shippingAddressId,
        String deliveryMessage
) {
}
