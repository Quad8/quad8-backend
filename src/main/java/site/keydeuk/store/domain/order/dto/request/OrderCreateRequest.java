package site.keydeuk.store.domain.order.dto.request;

public record OrderCreateRequest(
        Integer productId,
        Long switchOptionId,
        Integer quantity
) {
}
