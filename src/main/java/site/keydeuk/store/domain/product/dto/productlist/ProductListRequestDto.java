package site.keydeuk.store.domain.product.dto.productlist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@Setter
@AllArgsConstructor
public class ProductListRequestDto {
    @NotNull
    @Schema(description = "all, keyboard, keycap, switch, others", example = "all")
    private String keyword;

    @NotNull
    @Schema(description = "정렬(최신순 : createdAt_desc, 조회순 : views_desc, 가격 낮은순 : price_asc, 가격 높은순 : price_desc), 인기순 미구현", example = "views_desc")
    private String sort;


    @AssertTrue(message = "Invalid product type")
    private boolean isValidType() {
        return keyword != null && (keyword.equals("all") || keyword.equals("keyboard") || keyword.equals("keycap") || keyword.equals("switch") || keyword.equals("others"));
    }

}
