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

    private String thumnail;

    private int price;

    public RecentProductDto(Product product){
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        List<ProductImg> productImgs = product.getProductImgs();
        if (!productImgs.isEmpty()){
            this.thumnail = product.getProductImgs().get(0).getImgUrl();

        }else {
            log.info("product_{} is empty",product.getId());
        }
    }

}
