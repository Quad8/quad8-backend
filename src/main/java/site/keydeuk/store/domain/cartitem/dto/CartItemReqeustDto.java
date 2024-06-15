package site.keydeuk.store.domain.cartitem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemReqeustDto {

    @Schema(description = "상품 id", example = "1")
    @NotNull(message = "상품 아이디 필수 입력값 입니다.")
    private Integer productId;

    @Schema(description = "상품 수량", example = "1")
    @Min(value = 1,message = "최소 1개 이상 담아주세요")
    private int count;

    @Schema(description = "스위치 옵션 ID", example = "1")
    private Long switchOptionId;
}
