package site.keydeuk.store.domain.cartitem.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestDto {

    @Schema(description = "상품 수량", example = "1")
    @Min(value = 1,message = "최소 1개 이상 담아주세요")
    private int count;

    @Schema(description = "스위치 옵션 ID", example = "1")
    private Long switchOptionId;

}
