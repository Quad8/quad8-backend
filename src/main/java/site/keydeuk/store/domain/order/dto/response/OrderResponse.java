package site.keydeuk.store.domain.order.dto.response;

import lombok.Builder;
import site.keydeuk.store.entity.Order;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
        Long orderId,
        List<OrderItemResponse> orderItems,
        OrderStatus orderStatus,
        LocalDateTime purchaseDate,
        LocalDateTime confirmationDate
) {
}
