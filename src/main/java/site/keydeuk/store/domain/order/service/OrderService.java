package site.keydeuk.store.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.ErrorCode;
import site.keydeuk.store.domain.order.dto.response.OrderItemResponse;
import site.keydeuk.store.domain.order.dto.response.OrderResponse;
import site.keydeuk.store.domain.order.repository.OrderRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.entity.Order;
import site.keydeuk.store.entity.OrderItem;
import site.keydeuk.store.entity.Product;

import java.util.List;

import static site.keydeuk.store.common.response.ErrorCode.ORDER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Transactional
    public void deleteOrders(Long userId, List<Long> orderIds) {
        List<Order> orders = orderRepository.findAllById(orderIds).stream()
                .filter(order -> order.getUserId().equals(userId))
                .toList();

        if (orders.size() != orderIds.size()) {
            throw new CustomException(ORDER_NOT_FOUND);
        }
        orderRepository.deleteAll(orders);
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(this::toOrderItemResponse)
                .toList();
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderItems(orderItemResponses)
                .orderStatus(order.getStatus())
                .purchaseDate(order.getCreatedAt())
                .confirmationDate(order.getUpdatedAt()) //구매 확정 날짜 저장이지만 임시로
                .build();
    }

    private OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        Product product = productRepository.findById(orderItem.getProduct().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        //TODO: 스위치 옵션 id로 스위치 옵션 명 가져오기
//        Long switchOptionId = orderItem.getProduct().getProduct;
        String switchOption = "옵션";
        return OrderItemResponse.from(orderItem, product, switchOption);
    }
}
