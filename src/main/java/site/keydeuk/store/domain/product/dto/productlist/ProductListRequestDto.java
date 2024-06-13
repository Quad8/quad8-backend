package site.keydeuk.store.domain.product.dto.productlist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListRequestDto {
    @NotNull
    @Schema(description = " keyboard, keycap, switch, etc", example = "keyboard")
    private String keyword;

    @NotNull
    @Schema(description = "정렬(최신순 : createdAt_desc, 조회순 : views_desc, 가격 낮은순 : price_asc, 가격 높은순 : price_desc), 인기순 미구현", example = "views_desc")
    private String sort;

    @Schema(description = "현재 페이지, default 0", example = "0")
    private int page;

    @Schema(description = "개수, default 10", example = "10")
    private int size;

    @Schema(description = "제조사", example = "몬스타기어")
    private String company;

    @Schema(description = "(구현 XXXX)스위치", example = "적축")
    private String switchType;

    @Schema(description = "최소 가격", example = "1000")
    private Integer minPrice;

    @Schema(description = "최대 가격", example = "199000")
    private Integer maxPrice;

    public ProductListRequestDto(String keyword, String sort){
        this.keyword = keyword;
        this.sort = sort;
        this.page = 0;
        this.size = 10;
    }


    @AssertTrue(message = "Invalid product type")
    private boolean isValidType() {
        return keyword != null && ( keyword.equals("keyboard") || keyword.equals("keycap") || keyword.equals("switch") || keyword.equals("etc"));
    }

}
