package site.keydeuk.store.domain.order.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderCreateRequest(
        Integer productId,
        Long switchOptionId,
        @NotNull
        @Min(1)
        @Max(99)
        Integer quantity
) {
}
