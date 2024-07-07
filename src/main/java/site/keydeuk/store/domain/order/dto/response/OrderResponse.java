package site.keydeuk.store.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.keydeuk.store.entity.Order;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class OrderResponse {
    private final Long orderId;
    private final List<OrderItemResponse> orderItems;
    private final OrderStatus orderStatus;
    private final LocalDateTime purchaseDate;
    private final LocalDateTime confirmationDate;
    private final String deliveryMessage;
}
