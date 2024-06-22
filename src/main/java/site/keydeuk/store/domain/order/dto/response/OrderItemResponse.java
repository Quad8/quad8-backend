package site.keydeuk.store.domain.order.dto.response;

import lombok.Builder;
import site.keydeuk.store.entity.OrderItem;
import site.keydeuk.store.entity.Product;

@Builder
public record OrderItemResponse(
        Integer productId,
        String productImgUrl,
        String productName,
        String switchOption,
        Integer viewCount,
        Integer price
) {
    public static OrderItemResponse from(Product product, String switchOption) {
        return OrderItemResponse.builder()
                .productId(product.getId())
                .productImgUrl(product.getProductImgs().get(0).getImgUrl())
                .productName(product.getName())
                .switchOption(switchOption)
                .viewCount(product.getViews())
                .price(product.getPrice())
                .build();
    }

    public static OrderItemResponse from(OrderItem orderItem, String productImgUrl, String productName, String switchOption, Integer viewCount) {
        return OrderItemResponse.builder()
                .productId(orderItem.getProductId())
                .productImgUrl(productImgUrl)
                .productName(productName)
                .switchOption(switchOption)
                .viewCount(viewCount)
                .price(orderItem.getPrice())
                .build();
    }

}
