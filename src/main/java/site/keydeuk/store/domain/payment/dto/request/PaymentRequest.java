package site.keydeuk.store.domain.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "결제 요청 DTO")
public record PaymentRequest(
        @Schema(description = "주문 ID", example = "123")
        Long orderId,

        @Schema(description = "결제 키", example = "key_example")
        String paymentKey,

        @Schema(description = "결제 주문 ID", example = "order_123")
        String paymentOrderId,

        @Schema(description = "결제 금액", example = "1000")
        Long amount,
        @Schema(description = "결제 통화", example = "KRW")
        String currency
) {
}
