package site.keydeuk.store.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.product.dto.productlist.ProductListRequestDto;
import site.keydeuk.store.domain.product.service.ProductService;
@Slf4j
@Tag(name = "Product", description = "Product 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@RestController
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록 조회", description = "카테고리별(전체, 키보드, 키캡, 스위치, 기타용품) 상품 목록과 총 개수를 조회합니다.")
    @GetMapping("/get-list")
    public CommonResponse<?> getProductList(@ParameterObject @Valid ProductListRequestDto dto){
        //all : 전체, 1 keyboard, 2 keycap, 3 switch, 4-8 others
        // 필터 : 인기순(리뷰 많은? 구매 건? ), 조회순, 최신순, 가격 낮은 순, 가격 높은 순
        // 인기순 추후 구현
        String word = dto.getKeyword();
        String sort = dto.getSort();

        //log.info("keyword = {}",word);

        if (word.equals("all")){
            return CommonResponse.ok(productService.getProductAllList(sort));
        } else if (word.equals("keyboard")) {
            return CommonResponse.ok(productService.getProductListByCategory(1,sort));
        }else if (word.equals("keycap")) {
            return CommonResponse.ok(productService.getProductListByCategory(2,sort));
        }else if (word.equals("switch")) {
            return CommonResponse.ok(productService.getProductListByCategory(3,sort));
        }else if (word.equals("others")) {
            return CommonResponse.ok(productService.getProductListByCategory(4,sort));
        }

        return CommonResponse.fail("Invalid keyword");
    }

}
