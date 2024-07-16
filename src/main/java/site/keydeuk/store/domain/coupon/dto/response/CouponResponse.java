package site.keydeuk.store.domain.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CouponResponse(
        @Schema(description = "쿠폰 id", example = "1")
        Long id,
        @Schema(description = "쿠폰 이름", example = "깜짝 쿠폰")
        String name,
        @Schema(description = "쿠폰 가격", example = "4000")
        Integer price,
        @Schema(description = "주문 최소 금액", example = "40000")
        Integer minPrice,
        @Schema(description = "쿠폰 만료 기간", example = "2024-07-12T00:00:00")
        LocalDateTime expiredAt
) {
}
