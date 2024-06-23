package site.keydeuk.store.domain.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentResponse(
        @Schema(description = "결제 ID", example = "12345")
        Long id,

        @Schema(description = "주문 ID", example = "67890")
        Long orderId,

        @Schema(description = "결제 키", example = "payment_abcdef123456")
        String paymentKey,

        @Schema(description = "결제 방법", example = "CARD")
        String method,

        @Schema(description = "총 결제 금액", example = "10000")
        Long totalAmount,

        @Schema(description = "결제 상태", example = "COMPLETED")
        String status,

        @Schema(description = "결제 요청 시간", example = "2023-06-22T10:15:30")
        LocalDateTime requestedAt,

        @Schema(description = "결제 승인 시간", example = "2023-06-22T10:16:00")
        LocalDateTime approvedAt,

        @Schema(description = "마지막 거래 키", example = "trans_abcdef123456")
        String lastTransactionKey
) {

}
