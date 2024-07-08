package site.keydeuk.store.domain.order.dto.request;

public record OrderUpdateRequest(
        Long shippingAddressId,
        String deliveryMessage
) {
}
