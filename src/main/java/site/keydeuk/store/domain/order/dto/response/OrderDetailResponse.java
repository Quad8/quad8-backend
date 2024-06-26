package site.keydeuk.store.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.keydeuk.store.domain.shipping.dto.ShippingAddressDto;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class OrderDetailResponse{
    private final Long orderId;
    private final List<OrderItemResponse> orderItems;
    private final ShippingAddressDto shippingAddress;
    private final Long totalAmount;
    private final LocalDateTime purchaseDate;
    private final LocalDateTime confirmationDate;
}