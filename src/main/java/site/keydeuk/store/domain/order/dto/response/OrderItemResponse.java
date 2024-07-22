package site.keydeuk.store.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.keydeuk.store.entity.OrderItem;
import site.keydeuk.store.entity.Product;

import java.util.Optional;

@Builder
@Getter
@RequiredArgsConstructor
public class OrderItemResponse {
    private final Integer productId;
    private final String productImgUrl;
    private final String productName;
    private final Object switchOption;
    private final Integer quantity;
    private final Integer viewCount;
    private final Long price;
    public static OrderItemResponse from(Product product, Object switchOption, int quantity) {
        return OrderItemResponse.builder()
                .productId(product.getId())
                .productImgUrl(product.getProductImgs().get(0).getImgUrl())
                .productName(product.getName())
                .switchOption(switchOption)
                .quantity(quantity)
                .viewCount(product.getViews())
                .price(product.getPrice())
                .build();
    }

    public static OrderItemResponse from(OrderItem orderItem, String productImgUrl, String productName, Object switchOption, Integer viewCount) {
        return OrderItemResponse.builder()
                .productId(orderItem.getProductId())
                .productImgUrl(productImgUrl)
                .productName(productName)
                .switchOption(switchOption)
                .quantity(orderItem.getCount())
                .viewCount(viewCount)
                .price(orderItem.getPrice())
                .build();
    }

}
