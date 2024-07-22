package site.keydeuk.store.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateCouponRequest(
        @Schema(description = "쿠폰 이름", example = "깜짝 쿠폰")
        @NotEmpty(message = "쿠폰 이름은 필수 입력 항목입니다.")
        String name,
        @Schema(description = "쿠폰 가격", example = "4000")
        @NotNull(message = "쿠폰 가격은 필수 입력 항목입니다.")
        Long price,
        @Schema(description = "주문 최소 금액", example = "40000")
        @NotNull(message = "주문 최소 금액은 필수 입력 항목입니다.")
        Long minPrice,
        @Schema(description = "쿠폰 만료 기간", example = "2024-07-12T00:00:00")
        @NotNull(message = "쿠폰 만료 기간은 필수 입력 항목입니다.")
        LocalDateTime expiredDate,
        @Schema(description = "회원가입 쿠폰 여부", example = "true")
        Boolean isWelcome
) {
}
