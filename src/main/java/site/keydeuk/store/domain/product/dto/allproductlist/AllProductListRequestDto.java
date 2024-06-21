package site.keydeuk.store.domain.product.dto.allproductlist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllProductListRequestDto {

        @NotNull
        @Schema(description = "정렬(최신순 : createdAt_desc, 조회순 : views_desc, 가격 낮은순 : price_asc, 가격 높은순 : price_desc, 인기순 : popular)", example = "views_desc")
        private String sort;

        @Schema(description = "현재 페이지, default 0", example = "0")
        private int page;

        @Schema(description = "개수, default 10", example = "10")
        private int size;

        public AllProductListRequestDto(String sort){
            this.sort = sort;
            this.page = 0;
            this.size = 10;
        }



}


