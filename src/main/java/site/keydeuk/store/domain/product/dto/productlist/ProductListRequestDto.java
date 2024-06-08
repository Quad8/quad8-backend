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
    @Schema(description = "all, keyboard, keycap, switch, others", example = "all")
    private String keyword;

    @NotNull
    @Schema(description = "정렬(최신순 : createdAt_desc, 조회순 : views_desc, 가격 낮은순 : price_asc, 가격 높은순 : price_desc), 인기순 미구현", example = "views_desc")
    private String sort;

    @Schema(description = "현재 페이지, default 0", example = "0")
    private int page;

    @Schema(description = "개수, default 10", example = "10")
    private int size;

    public ProductListRequestDto(String keyword, String sort){
        this.keyword = keyword;
        this.sort = sort;
        this.page = 0;
        this.size = 10;
    }


    @AssertTrue(message = "Invalid product type")
    private boolean isValidType() {
        return keyword != null && (keyword.equals("all") || keyword.equals("keyboard") || keyword.equals("keycap") || keyword.equals("switch") || keyword.equals("others"));
    }

}
