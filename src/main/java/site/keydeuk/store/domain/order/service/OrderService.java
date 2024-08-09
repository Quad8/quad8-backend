package site.keydeuk.store.domain.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.order.dto.OrderDto;
import site.keydeuk.store.domain.order.dto.request.OrderCreateRequest;
import site.keydeuk.store.domain.order.dto.request.OrderUpdateRequest;
import site.keydeuk.store.domain.order.dto.response.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final CustomObjectRepository customObjectRepository;
    private final PaymentRepository paymentRepository;
    private final ShippingRepository shippingRepository;
    private final ObjectMapper objectMapper;

    /**
     * 주문 생성
     * 주문하기 -> 결제 정보 페이지 로 넘어갈때 주문 정보 저장
     * 저장된 주문 정보는 결제 승인시 사용
     *
     * @param requests 주문할 상품들 정보
     */
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

    /**
     * 결제 정보 조회
     * 결제 페이지를 띄울때 필요한 데이터 조회
     *
     * @param orderId 주문 저장시 반환된 주문 아이디
     */
    @Transactional(readOnly = true)
    public OrderCreateResponse getOrderResponse(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        validateReadyOrder(order);

        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemResponse> orderItemResponses = orderItems.stream()
                .map(this::toOrderItemResponse)
                .toList();

        Long shippingAddressId = order.getShippingAddressId();
        ShippingAddress shippingAddress = ShippingAddress.NULL;
        if (shippingAddressId != null && shippingAddressId != 0) {
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

    /**
     * 결제 전 주문 정보 업데이트
     * @param orderId 주문 id
     * @param request 바뀐 정보들
     */
    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        order.updateShippingInfo(request.shippingAddressId(), request.deliveryMessage());
        return toOrderResponse(order);
    }

    /**
     * 결제 완료 된 주문 전체 조회
     *
     * @param userId 유저 아이디
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(Long userId, Pageable pageable, LocalDateTime startDate, LocalDateTime endDate) {
        Page<Order> orders = orderRepository.findByUserIdAndStatusNotAndCreatedAtBetween(userId, OrderStatus.READY, startDate, endDate, pageable);
        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }

    /**
     * 결제 완료 된 주문 상세 조회
     *
     * @param userId  유저 아이디
     * @param orderId 주문 아이디
     */
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
                .paymentOrderId(order.getPaymentOrderId())
                .orderItems(orderItemResponses)
                .shippingAddress(shippingAddressDto)
                .deliveryMessage(order.getDeliveryMessage())
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

    private Long getPrice(Integer productId) {
        if (productId > 100000) {
            CustomOption customOption = customRepository.findById(productId)
                    .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
            log.info("{}", customOption.getPrice());
            return (long) customOption.getPrice();
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
        return product.getPrice();
    }

    /**
     * 구매 확정 된 주문 전체 조회
     *
     * @param userId 유저 아이디
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrdersStatusConfirmedByUserId(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId, OrderStatus.CONFIRMED);
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
                .deliveryMessage(order.getDeliveryMessage())
                .build();
    }

    private OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if (orderItem.getProductId() > 100000) {
            CustomObject object = customObjectRepository.findById(orderItem.getProductId())
                    .orElse(null);
            CustomOption customOption = customRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new CustomException(OPTION_NOT_FOUND));
            Object switchOption = customOption.toString();

            if (object != null) {
                Object individualColor = object.getObjects();
                switchOption = new OrderProductOptionResponse(individualColor, customOption, objectMapper);
                log.info("{}", switchOption);
            }
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
        return OrderItemResponse.from(product, switchOption, orderItem.getCount());
    }

    private static void validateReadyOrder(Order order) {
        if (order.getStatus() != OrderStatus.READY) {
            throw new CustomException(READY_ORDER_NOT_FOUND);
        }
    }
}
