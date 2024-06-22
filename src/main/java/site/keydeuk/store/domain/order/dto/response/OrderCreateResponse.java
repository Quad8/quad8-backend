package site.keydeuk.store.domain.order.dto.response;

import lombok.Builder;
import site.keydeuk.store.domain.shipping.dto.response.ShippingAddressResponse;

import java.util.List;

@Builder
public record OrderCreateResponse(
        Long orderId,
        String paymentOrderId,
        List<OrderItemResponse> orderItemResponses,
        ShippingAddressResponse shippingAddressResponse,
        Long totalPrice
) {
}
