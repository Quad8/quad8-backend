package site.keydeuk.store.domain.search.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.ProductImg;

import java.util.List;
@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private int productId;

    private String name;

    private String thumbnail;

    private int price;

    private Long reviewCount;

    private boolean isLiked;

    private String category;

    public ProductResponseDto(Product product,boolean isLiked,Long reviewCount){
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.reviewCount = reviewCount;
        this.isLiked = isLiked;
        List<ProductImg> productImgs = product.getProductImgs();
        if (!productImgs.isEmpty()){
            this.thumbnail = product.getProductImgs().get(0).getImgUrl();
        }else {
            log.error("product_{} is empty",product.getId());
        }
        if (product.getProductCategory().getId() >3){
            this.category = "etc";
        }else {
            this.category = product.getProductCategory().getName();
        }
    }
}
