package site.keydeuk.store.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.ErrorCode;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.order.dto.request.OrderCreateRequest;
import site.keydeuk.store.domain.order.dto.response.OrderCreateResponse;
import site.keydeuk.store.domain.order.dto.response.OrderDetailResponse;
import site.keydeuk.store.domain.order.dto.response.OrderItemResponse;
import site.keydeuk.store.domain.order.dto.response.OrderResponse;
import site.keydeuk.store.domain.order.repository.OrderItemsRepository;
import site.keydeuk.store.domain.order.repository.OrderRepository;
import site.keydeuk.store.domain.payment.repository.PaymentRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.productswitchoption.repository.ProductSwitchOptionRepository;
import site.keydeuk.store.domain.shipping.dto.ShippingAddressDto;
import site.keydeuk.store.domain.shipping.dto.response.ShippingAddressResponse;
import site.keydeuk.store.domain.shipping.repository.ShippingRepository;
import site.keydeuk.store.entity.*;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static site.keydeuk.store.common.response.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private final ProductSwitchOptionRepository productSwitchOptionRepository;
    private final CustomRepository customRepository;
    private final PaymentRepository paymentRepository;
    private final ShippingRepository shippingRepository;

    @Transactional
    public Long createOrder(Long userId, List<OrderCreateRequest> requests) {
        Long shippingAddressId = shippingRepository.findByUserIdAndIsDefault(userId, true)
                .orElse(ShippingAddress.NULL)
                .getId();

        Order order = Order.builder()
                .userId(userId)
                .paymentOrderId(UUID.randomUUID().toString())
                .shippingAddressId(shippingAddressId)
                .orderItems(new ArrayList<>())
                .status(OrderStatus.READY)
                .build();

        Order savedOrder = orderRepository.save(order);
        List<OrderItem> orderItems = requests.stream()
                .map(
                        request -> OrderItem.builder()
                                .order(savedOrder)
                                .count(request.quantity())
                                .productId(request.productId())
                                .price(getPrice(request.productId()))
                                .switchOptionId(request.switchOptionId())
                                .build()
                )
                .toList();
        savedOrder.addOrderItems(orderItems);
        orderItemsRepository.saveAll(orderItems);

        return savedOrder.getId();
    }

    // 결제 페이지 띄울때의 응답
    @Transactional(readOnly = true)
    public OrderCreateResponse getOrderResponse(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemResponse> orderItemResponses = orderItems.stream()
                .map(this::toOrderItemResponse)
                .toList();

        Long shippingAddressId = order.getShippingAddressId();
        ShippingAddress shippingAddress = ShippingAddress.NULL;
        if (shippingAddressId != null && shippingAddressId !=0) {
            shippingAddress = shippingRepository.findById(shippingAddressId)
                    .orElseThrow(() -> new CustomException(SHIPPING_NOT_FOUND));
        }

        return OrderCreateResponse.builder()
                .orderId(order.getId())
                .paymentOrderId(order.getPaymentOrderId())
                .orderItemResponses(orderItemResponses)
                .totalPrice(order.getTotalPrice())
                .shippingAddressResponse(ShippingAddressResponse.from(shippingAddress))
                .build();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }

    // 결제 완료 된 주문 건
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

        if (!order.getUserId().equals(userId)) {
            throw new CustomException(USER_ORDER_NOT_MATCH);
        }

        ShippingAddress shippingAddress = shippingRepository.findById(order.getShippingAddressId())
                .orElseThrow(() -> new CustomException(SHIPPING_NOT_FOUND));
        ShippingAddressDto shippingAddressDto = ShippingAddressDto.from(shippingAddress);


        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new CustomException(PAYMENT_NOT_FOUND));

        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(this::toOrderItemResponse)
                .toList();

        return OrderDetailResponse.builder()
                .orderId(order.getId())
                .orderItems(orderItemResponses)
                .shippingAddress(shippingAddressDto)
                .totalAmount(payment.getTotalAmount())
                .purchaseDate(payment.getApprovedAt())
                .confirmationDate(order.getUpdatedAt())
                .build();
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

    private Integer getPrice(Integer productId) {
        if (productId > 100000) {
            CustomOption customOption = customRepository.findById(productId)
                    .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
            log.info("{}",customOption.getPrice());
            return customOption.getPrice();
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
        return product.getPrice();
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
        if (orderItem.getProductId() > 100000) {
            CustomOption customOption = customRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new CustomException(OPTION_NOT_FOUND));
            String switchOption = customOption.toString();
            return OrderItemResponse.from(orderItem, customOption.getImgUrl(), "커스텀 키보드", switchOption, 0);
        }
        Integer productId = orderItem.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
        Long switchOptionId = orderItem.getSwitchOptionId();
        String switchOption = "";
        if (switchOptionId != null) {
            ProductSwitchOption productSwitchOption = productSwitchOptionRepository.findById(switchOptionId).orElseThrow(
                    () -> new CustomException(OPTION_NOT_FOUND)
            );
            switchOption = productSwitchOption.getOptionName();
        }
        return OrderItemResponse.from(product, switchOption);
    }
}
