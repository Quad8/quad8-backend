package site.keydeuk.store.domain.product.dto.productlist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListRequestDto {

    @NotNull
    @Schema(description = "정렬(최신순 : createdAt_desc, 조회순 : views_desc, 가격 낮은순 : price_asc, 가격 높은순 : price_desc, 인기순 : popular)", example = "views_desc")
    private String sort;

    @Schema(description = "현재 페이지, default 0", example = "0")
    private int page;

    @Schema(description = "개수, default 10", example = "10")
    private int size;

    @Schema(description = "제조사", example = "몬스타기어")
    private List<String> companies;

    @Schema(description = "스위치", example = "적축")
    private List<String> switchTypes;

    @Schema(description = "최소 가격", example = "1000")
    private Long minPrice;

    @Schema(description = "최대 가격", example = "199000")
    private Long maxPrice;

    public ProductListRequestDto( String sort){
        this.sort = sort;
        this.page = 0;
        this.size = 10;
    }

}
