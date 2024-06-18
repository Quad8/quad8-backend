package site.keydeuk.store.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.product.dto.allproductlist.AllProductListRequestDto;
import site.keydeuk.store.domain.product.dto.productdetail.ProductDetailResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListRequestDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListResponseDto;
import site.keydeuk.store.domain.product.service.ProductService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.user.service.UserService;


@Slf4j
@Tag(name = "Product", description = "Product 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@RestController
public class ProductController {

    private final ProductService productService;
    private final LikesService likesService;

    @Operation(summary = "상품 상세 조회", description = "상품 Id로 상세 정보를 조회합니다. (미구현 : 리뷰 )")
    @Parameter(name = "id", description = "상품 ID", example = "11")
    @GetMapping("/get-detail-info/{id}")
    public CommonResponse<?> getProductDetailById(@PathVariable("id") Integer id,@AuthenticationPrincipal PrincipalDetails principalDetails){

        ProductDetailResponseDto dto = productService.getProductDetailById(id);

        if (principalDetails != null){
            Long userId = principalDetails.getUserId();
            boolean isLiked = likesService.existsByUserIdAndProductId(userId,id);
            dto.setLiked(isLiked);
        }
        return CommonResponse.ok(dto);
    }

    @Operation(summary = "상품 전체 목록 조회", description = "전체 상품 목록과 총 개수를 조회합니다.(인기순 미구현)")
    @GetMapping("/get/all-list")
    public CommonResponse<?> getAllProductList(@ParameterObject @Valid AllProductListRequestDto dto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        //all : 전체
        // 필터 : 인기순(리뷰 많은? 구매 건? ), 조회순, 최신순, 가격 낮은 순, 가격 높은 순
        // 인기순 추후 구현

        //paging 처리
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

        return CommonResponse.ok(productService.getProductAllList(dto.getSort(), pageable,principalDetails.getUserId()));

    }

    @Operation(summary = "(스위치 검색 미구현!!)카테고리별 상품 목록 조회", description = "카테고리별(키보드, 키캡, 스위치, 기타용품) 상품 목록과 총 개수를 조회합니다.(인기순 미구현)")
    @GetMapping("/get/category-list")
    public CommonResponse<?> getProductList(@ParameterObject @Valid ProductListRequestDto dto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        //all : 전체, 1 keyboard, 2 keycap, 3 switch, 4-8 etc
        // 필터 : 인기순(리뷰 많은? 구매 건? ), 조회순, 최신순, 가격 낮은 순, 가격 높은 순
        // 인기순 추후 구현
        String word = dto.getKeyword();
        Long userId = principalDetails.getUserId();

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

        if (word.equals("keyboard")) {
            return CommonResponse.ok(productService.getProductListByCategoryAndFilters(
                    1, 1, dto.getCompany(), dto.getMinPrice(), dto.getMaxPrice(), dto.getSort(), pageable,userId));
        }else if (word.equals("keycap")) {
            return CommonResponse.ok(productService.getProductListByCategoryAndFilters(
                    2, 2, dto.getCompany(), dto.getMinPrice(), dto.getMaxPrice(), dto.getSort(), pageable,userId));
        }else if (word.equals("switch")) {
            return CommonResponse.ok(productService.getProductListByCategoryAndFilters(
                    3, 3, dto.getCompany(), dto.getMinPrice(), dto.getMaxPrice(), dto.getSort(), pageable,userId));
        }else if (word.equals("etc")) {
            return CommonResponse.ok(productService.getProductListByCategoryAndFilters(
                    4, 8, dto.getCompany(), dto.getMinPrice(), dto.getMaxPrice(), dto.getSort(), pageable,userId));
        }
        return CommonResponse.fail("Invalid keyword");
    }

    @Operation(summary = "메인페이지 키득 PICK 상품 목록", description = "메인페이지 키득 PICK 상품 목록을 조회합니다.")
    @Parameter(name = "param", description = "직장인을 위한 -> 저소음, 가성비 -> 가성비, 타건감 -> 청축", example = "저소음")
    @GetMapping("/get/keydeuk-pick")
    public CommonResponse<?> getKeydeukPick(@RequestParam String param,@AuthenticationPrincipal PrincipalDetails principalDetails){

        if (!param.equals("저소음") && !param.equals("청축") && !param.equals("가성비")){
           return CommonResponse.fail("잘못된 요청입니다.");
        }
        return CommonResponse.ok(productService.getProductListByswitch(param,principalDetails.getUserId()));
    }
    @Operation(summary = "메인페이지 키득 BEST 상품 목록", description ="메인페이지 키득 BEST 상품 목록을 조회합니다.")
    @GetMapping("/get/keyduek-best")
    public CommonResponse<?> getKeydeukBest(@AuthenticationPrincipal PrincipalDetails principalDetails){
        /** 구매나 리뷰 완료되면 로직 재구성 */
        return CommonResponse.ok(productService.getBestProductList(principalDetails.getUserId()));
    }

}
