package site.keydeuk.store.domain.product.dto.productdetail;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Setter;
import site.keydeuk.store.entity.Product;
import java.util.List;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductDetailResponseDto {

    private int id;

    private String name;

    private int price;

    private int reviewscount;

    private int views;

    private double scope;

    private String detailsImg;

    private List thubmnailList;

    private List optionList;

    public ProductDetailResponseDto(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.reviewscount = 999;
        this.views = product.getViews()+1;
        this.scope = 4.5;
        this.detailsImg = product.getDetailUrl();
        this.thubmnailList = product.getProductImgs();
        if(!product.getSwitchOptions().isEmpty()){
            this.optionList = product.getSwitchOptions();

        }else {
            this.optionList = null;
        }
    }

}
