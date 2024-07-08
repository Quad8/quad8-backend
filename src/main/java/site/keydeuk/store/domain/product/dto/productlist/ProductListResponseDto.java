package site.keydeuk.store.domain.product.dto.productlist;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.ProductImg;

import java.util.List;
@Slf4j
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductListResponseDto {

    private int id;

    private String name;

    private int price;

    private Long reviewscount;

    private int views;

    private String thumbnail;

    private boolean isLiked;

    private String category;

    public ProductListResponseDto(Product product,boolean isLiked,Long reviewCount){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.reviewscount = reviewCount;
        this.views = product.getViews();
        this.isLiked = isLiked;
        if (product.getProductCategory().getId() >3){
            this.category = "etc";
        }else {
            this.category = product.getProductCategory().getName();
        }
        List<ProductImg> productImgs = product.getProductImgs();
        if (!productImgs.isEmpty()){
            this.thumbnail = product.getProductImgs().get(0).getImgUrl();

        }else {
            log.error("product_{} is empty",product.getId());
        }
    }

}
