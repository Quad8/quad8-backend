package site.keydeuk.store.domain.customoption.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.ProductImg;

import java.util.List;
@Slf4j
@Setter
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OptionProductsResponseDto {

    private Integer id;

    private String name;

    private String categoryName;

    private int price;

    private String thumbnail;

    public OptionProductsResponseDto(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.categoryName = product.getProductCategory().getName();

        List<ProductImg> productImgs = product.getProductImgs();
        if (!productImgs.isEmpty()){
            this.thumbnail = product.getProductImgs().get(0).getImgUrl();
        }else {
            log.info("product_{} is empty",product.getId());
        }
    }

}
