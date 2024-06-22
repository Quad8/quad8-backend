package site.keydeuk.store.domain.product.dto.productdetail;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Setter;
import site.keydeuk.store.domain.productswitchoption.dto.ProductSwitchOptionDto;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.ProductSwitchOption;

import java.util.ArrayList;
import java.util.List;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductDetailResponseDto {

    private int id;

    private String name;

    private Integer price;

    private Long reviewscount;

    private int views;

    private double scope;

    private String detailsImg;

    private List thubmnailList;

    private List optionList;

    private String categoryName;

    private boolean isLiked;

    public ProductDetailResponseDto(Product product,Long reviewCount, Double scope){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.reviewscount = reviewCount;
        this.views = product.getViews()+1;
        this.scope = scope;
        this.detailsImg = product.getDetailUrl();
        this.thubmnailList = product.getProductImgs();
        if(!product.getSwitchOptions().isEmpty()){
            this.optionList = new ArrayList<>();
            for (ProductSwitchOption switchOption : product.getSwitchOptions()){
                this.optionList.add(new ProductSwitchOptionDto(switchOption));
            }
        }else {
            this.optionList = null;
        }
        this.categoryName = product.getProductCategory().getName();
        this.isLiked = false;
    }

}
