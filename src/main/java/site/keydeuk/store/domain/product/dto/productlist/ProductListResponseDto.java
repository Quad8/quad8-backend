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

    private int reviewscount;

    private int views;

    private String thumbnail;

    private boolean isLiked;

    public ProductListResponseDto(Product product,boolean isLiked){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.reviewscount = 999;
        this.views = product.getViews();
        this.isLiked = isLiked;

        List<ProductImg> productImgs = product.getProductImgs();
        if (!productImgs.isEmpty()){
            this.thumbnail = product.getProductImgs().get(0).getImgUrl();

        }else {
            log.info("product_{} is empty",product.getId());
        }
    }

}
