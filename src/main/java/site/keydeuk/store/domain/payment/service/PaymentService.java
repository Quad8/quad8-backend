package site.keydeuk.store.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.cart.repository.CartRepository;
import site.keydeuk.store.domain.order.dto.response.OrderDetailResponse;
import site.keydeuk.store.domain.order.repository.OrderRepository;
import site.keydeuk.store.domain.order.service.OrderService;
import site.keydeuk.store.domain.payment.PaymentClient;
import site.keydeuk.store.domain.payment.dto.request.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.request.PaymentRequest;
import site.keydeuk.store.domain.payment.dto.response.PaymentConfirmResponse;
import site.keydeuk.store.domain.payment.dto.response.PaymentResponse;
import site.keydeuk.store.domain.payment.dto.response.PaymentSuccessResponse;
import site.keydeuk.store.domain.payment.repository.PaymentRepository;
import site.keydeuk.store.entity.*;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.util.List;

import static site.keydeuk.store.common.response.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;
    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    /**
     * 결제 승인
     * @param request 결제 승인을 위한 값들: 결제 키, 결제 주문 아이디, 총 금액
     */
    @Transactional
    public PaymentResponse confirm(PaymentRequest request) {
        String paymentOrderId = request.paymentOrderId();
        Order order = orderRepository.findByPaymentOrderId(paymentOrderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

        validateOrder(order);
        validateAmount(request, order);

        PaymentConfirmRequest confirmRequest = PaymentConfirmRequest.from(request);
        PaymentConfirmResponse confirmResponse = paymentClient.confirm(confirmRequest);

        Payment payment = Payment.builder()
                .order(order)
                .paymentKey(confirmResponse.getPaymentKey())
                .method(confirmResponse.getMethod())
                .totalAmount(confirmRequest.amount())
                .status(confirmResponse.getStatus().name())
                .requestedAt(confirmResponse.getRequestedAt().toLocalDateTime().plusHours(15))
                .approvedAt(confirmResponse.getApprovedAt().toLocalDateTime().plusHours(15))
                .lastTransactionKey(confirmResponse.getLastTransactionKey())
                .build();
        Payment savedPayment = paymentRepository.save(payment);

        return PaymentResponse.builder()
                .id(savedPayment.getId())
                .orderId(savedPayment.getOrder().getId())
                .paymentOrderId(savedPayment.getOrder().getPaymentOrderId())
                .paymentKey(savedPayment.getPaymentKey())
                .method(savedPayment.getMethod())
                .totalAmount(savedPayment.getTotalAmount())
                .status(savedPayment.getStatus())
                .requestedAt(savedPayment.getRequestedAt())
                .approvedAt(savedPayment.getApprovedAt())
                .build();
    }

    /**
     * 결제 결과 성공시 로직
     * @param userId 사용자 id
     * @param paymentKey toss가 제공하는 결제 키
     * @param paymentOrderId 결제 주문 id
     */
    @Transactional
    public PaymentSuccessResponse paymentSuccess(Long userId, String paymentKey, String paymentOrderId) {
        Payment payment = getPaymentByPaymentKey(paymentKey);
        Order order = payment.getOrder();

        if (!order.getPaymentOrderId().equals(paymentOrderId)) {
            throw new CustomException(PAYMENT_ORDER_NOT_MATCH);
        }
        order.updateStatus(OrderStatus.PAYMENT_COMPLETED);

        List<Integer> productIdsInOrder = order.getOrderItems().stream()
                .map(OrderItem::getProductId)
                .toList();

        Cart cart = cartRepository.findByUserId(userId);
        List<CartItem> cartItems = cart.getCartItems();

        List<CartItem> itemsToRemove = cartItems.stream()
                .filter(cartItem -> {
                    if (cartItem instanceof CartItemWithProduct productItem) {
                        return productIdsInOrder.contains(productItem.getProduct().getId());
                    } else if (cartItem instanceof CartItemWithCustom customItem) {
                        return productIdsInOrder.contains(customItem.getCustomOption().getId());
                    }
                    return false;
                })
                .toList();

        int removedCount = itemsToRemove.size();
        cartItems.removeAll(itemsToRemove);
        cart.updateTotalCount(removedCount);

        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .paymentOrderId(payment.getOrder().getPaymentOrderId())
                .paymentKey(payment.getPaymentKey())
                .method(payment.getMethod())
                .totalAmount(payment.getTotalAmount())
                .status(payment.getStatus())
                .requestedAt(payment.getRequestedAt())
                .approvedAt(payment.getApprovedAt())
                .build();
        OrderDetailResponse orderDetailResponse = orderService.getOrder(userId, savedOrder.getId());
        return PaymentSuccessResponse.of(paymentResponse, orderDetailResponse);
    }

    private Payment getPaymentByPaymentKey(String paymentKey) {
        return paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new CustomException(PAYMENT_NOT_FOUND));
    }

    private static void validateOrder(Order order) {
        if (order.getStatus() != OrderStatus.READY) {
            throw new CustomException(READY_ORDER_NOT_FOUND);
        }
    }

    private static void validateAmount(PaymentRequest request, Order order) {
        if (!order.getTotalPrice().equals(request.amount())) {
            throw new CustomException(INVALID_PAYMENT_AMOUNT_ERROR);
        }
    }

}
