package site.keydeuk.store.domain.customoption.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class OptionProductsResponseDto {

    private Integer id;

    private String name;

    private String categoryName;

    private int price;

    private String thumbnail;

}
