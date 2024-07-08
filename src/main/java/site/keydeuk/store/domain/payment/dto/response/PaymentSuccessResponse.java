package site.keydeuk.store.domain.payment.dto.response;

import lombok.Builder;
import site.keydeuk.store.domain.order.dto.response.OrderDetailResponse;

@Builder
public record PaymentSuccessResponse(
        PaymentResponse paymentResponse,
        OrderDetailResponse orderDetailResponse
) {
    public static PaymentSuccessResponse of(PaymentResponse paymentResponse, OrderDetailResponse orderDetailResponse) {
        return PaymentSuccessResponse.builder()
                .paymentResponse(paymentResponse)
                .orderDetailResponse(orderDetailResponse)
                .build();
    }
}
