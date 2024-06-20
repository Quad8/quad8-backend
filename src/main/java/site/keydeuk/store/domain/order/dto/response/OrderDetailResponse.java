package site.keydeuk.store.domain.order.dto.response;

import lombok.Builder;
import site.keydeuk.store.domain.shipping.dto.ShippingAddressDto;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDetailResponse(
        Long orderId,
        List<OrderItemResponse> orderItems,
        ShippingAddressDto shippingAddress,
        Long totalAmount,
        LocalDateTime purchaseDate,
        LocalDateTime confirmationDate
) {
}