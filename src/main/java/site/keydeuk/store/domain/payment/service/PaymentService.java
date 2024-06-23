package site.keydeuk.store.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.order.repository.OrderRepository;
import site.keydeuk.store.domain.payment.PaymentClient;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmRequest;
import site.keydeuk.store.domain.payment.dto.PaymentConfirmResponse;
import site.keydeuk.store.domain.payment.dto.PaymentResponse;
import site.keydeuk.store.domain.payment.dto.request.PaymentRequest;
import site.keydeuk.store.domain.payment.repository.PaymentRepository;
import site.keydeuk.store.entity.Order;
import site.keydeuk.store.entity.Payment;
import site.keydeuk.store.entity.enums.OrderStatus;

import static site.keydeuk.store.common.response.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;
    private final OrderRepository orderRepository;

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
                .paymentKey(savedPayment.getPaymentKey())
                .method(savedPayment.getMethod())
                .totalAmount(savedPayment.getTotalAmount())
                .status(savedPayment.getStatus())
                .requestedAt(savedPayment.getRequestedAt())
                .approvedAt(savedPayment.getApprovedAt())
                .build();
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

    public void paymentSuccess(Long paymentId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(PAYMENT_NOT_FOUND));
        order.updateStatus(OrderStatus.PAYMENT_COMPLETED);
    }

}
