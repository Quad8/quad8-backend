package site.keydeuk.store.domain.productswitchoption.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.keydeuk.store.entity.ProductSwitchOption;

@Getter
@Setter
public class ProductSwitchOptionDto {

    private Long id;

    private String optionName;

    public ProductSwitchOptionDto(ProductSwitchOption switchOption){
        this.id = switchOption.getId();
        this.optionName = switchOption.getOptionName();
    }
}
