package site.keydeuk.store.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.product.dto.allproductlist.AllProductListRequestDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListRequestDto;
import site.keydeuk.store.domain.product.service.ProductService;


@Slf4j
@Tag(name = "Product", description = "Product 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@RestController
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 상세 조회", description = "상품 Id로 상세 정보를 조회합니다. (미구현 : 리뷰 & 찜 관련 )")
    @Parameter(name = "id", description = "상품 ID", example = "11")
    @GetMapping("/get-detail-info/{id}")
    public CommonResponse<?> getProductDetailById(@PathVariable("id") Integer id){
        return CommonResponse.ok(productService.getProductDetailById(id));
    }

    @Operation(summary = "상품 전체 목록 조회", description = "전체 상품 목록과 총 개수를 조회합니다.(인기순 미구현)")
    @GetMapping("/get/all-list")
    public CommonResponse<?> getAllProductList(@ParameterObject @Valid AllProductListRequestDto dto){
        //all : 전체
        // 필터 : 인기순(리뷰 많은? 구매 건? ), 조회순, 최신순, 가격 낮은 순, 가격 높은 순
        // 인기순 추후 구현

        //paging 처리
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

        return CommonResponse.ok(productService.getProductAllList(dto.getSort(), pageable));

    }

    @Operation(summary = "카테고리별 상품 목록 조회", description = "카테고리별(키보드, 키캡, 스위치, 기타용품) 상품 목록과 총 개수를 조회합니다.(인기순)")
    @GetMapping("/get/category-list")
    public CommonResponse<?> getProductList(@ParameterObject @Valid ProductListRequestDto dto){
        //all : 전체, 1 keyboard, 2 keycap, 3 switch, 4-8 etc
        // 필터 : 인기순(리뷰 많은? 구매 건? ), 조회순, 최신순, 가격 낮은 순, 가격 높은 순
        // 인기순 추후 구현
        String word = dto.getKeyword();
        String sort = dto.getSort();

        //log.info("keyword = {}",word);

        //paging 처리
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

        if (word.equals("keyboard")) {
            return CommonResponse.ok(productService.getProductListByCategory(1,sort,pageable));
        }else if (word.equals("keycap")) {
            return CommonResponse.ok(productService.getProductListByCategory(2,sort,pageable));
        }else if (word.equals("switch")) {
            return CommonResponse.ok(productService.getProductListByCategory(3,sort,pageable));
        }else if (word.equals("etc")) {
            return CommonResponse.ok(productService.getProductListByCategory(4,sort,pageable));
        }

        return CommonResponse.fail("Invalid keyword");
    }


}
