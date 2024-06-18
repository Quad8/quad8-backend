package site.keydeuk.store.domain.cartitem.dto.delete;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartItemRequestDto {

    @Schema(description = "cartItem Id", example = "1,2,3")
    private List<Long> deletedProducts;
}
