package site.keydeuk.store.domain.cart.dto.cartlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import site.keydeuk.store.domain.cartitem.dto.CartCustomResponseDto;
import site.keydeuk.store.domain.cartitem.dto.CartProductResponseDto;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartByUserResponseDto {

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("CUSTOM")
    private List<CartCustomResponseDto> customProducts;

    @JsonProperty("SHOP")
    private List<CartProductResponseDto> shopProducts;

}
