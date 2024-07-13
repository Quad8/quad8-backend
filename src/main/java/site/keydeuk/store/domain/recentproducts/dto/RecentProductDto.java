package site.keydeuk.store.domain.recentproducts.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.ProductImg;

import java.util.List;
@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentProductDto {

    private Integer productId;

    private String name;

    private String thumbnail;

    private int price;

    private String category;

    private boolean isLiked;

    private Long reviewCount;

    public RecentProductDto(Product product,boolean isLiked, Long reviewCount){
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.isLiked = isLiked;
        this.reviewCount = reviewCount;
        if (product.getProductCategory().getId() >3){
            this.category = "etc";
        }else {
            this.category = product.getProductCategory().getName();
        }
        List<ProductImg> productImgs = product.getProductImgs();
        if (!productImgs.isEmpty()){
            this.thumbnail = product.getProductImgs().get(0).getImgUrl();

        }else {
            log.info("product_{} is empty",product.getId());
        }
    }

}
