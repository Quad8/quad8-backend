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

import static site.keydeuk.store.common.response.ErrorCode.COMMON_INVALID_PARAMETER;


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
    @GetMapping("/{id}")
    public CommonResponse<?> getProductDetailById(@PathVariable("id") Integer id,@AuthenticationPrincipal PrincipalDetails principalDetails){

        ProductDetailResponseDto dto = productService.getProductDetailById(id);

        if (principalDetails != null){
            Long userId = principalDetails.getUserId();
            boolean isLiked = likesService.existsByUserIdAndProductId(userId,id);
            dto.setLiked(isLiked);
        }
        return CommonResponse.ok(dto);
    }

    @Operation(summary = "상품 전체 목록 조회", description = "전체 상품 목록과 총 개수를 조회합니다.")
    @GetMapping("/all")
    public CommonResponse<?> getAllProductList(@ParameterObject @Valid AllProductListRequestDto dto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        // 필터 : 인기순(구매순? ), 조회순, 최신순, 가격 낮은 순, 가격 높은 순
        Long userId = null;
        if (principalDetails!= null){
            userId = principalDetails.getUserId();
        }

        //paging 처리
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

        return CommonResponse.ok(productService.getProductAllList(dto.getSort(), pageable,userId));

    }

    @Operation(summary = "카테고리별 상품 목록 조회", description = "카테고리별(키보드, 키캡, 스위치, 기타용품) 상품 목록과 총 개수를 조회합니다.")
    @GetMapping("/category/{category}")
    @Parameter(name = "category", description = " keyboard, keycap, switch, etc", example = "keyboard")
    public CommonResponse<?> getProductList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @PathVariable("category") String category,
                                            @ParameterObject @Valid ProductListRequestDto dto ){
        if (!(category.equals("keyboard") || category.equals("keycap") || category.equals("switch") || category.equals("etc"))){
            throw new CustomException(COMMON_INVALID_PARAMETER);
        }
        // 필터 : 인기순(구매 건 ), 조회순, 최신순, 가격 낮은 순, 가격 높은 순

        Long userId = null;
        if (principalDetails!= null){
            userId = principalDetails.getUserId();
        }

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        int minPrice = dto.getMinPrice() != null ? dto.getMinPrice() : 0;
        int maxPrice = dto.getMaxPrice() != null ? dto.getMaxPrice() : Integer.MAX_VALUE;


        return CommonResponse.ok(productService.getProductListByCategory(category,dto.getCompanies(), dto.getSwitchTypes(), minPrice, maxPrice,pageable));

    }

    @Operation(summary = "메인페이지 키득 PICK 상품 목록", description = "메인페이지 키득 PICK 상품 목록을 조회합니다.")
    @Parameter(name = "param", description = "직장인을 위한 -> 저소음, 가성비 -> 가성비, 타건감 -> 청축", example = "저소음")
    @GetMapping("/get/keydeuk-pick")
    public CommonResponse<?> getKeydeukPick(@RequestParam String param,@AuthenticationPrincipal PrincipalDetails principalDetails){

        if (!param.equals("저소음") && !param.equals("청축") && !param.equals("가성비")){
           return CommonResponse.fail("잘못된 요청입니다.");
        }
        Long userId = null;
        if (principalDetails!= null){
            userId = principalDetails.getUserId();
        }
        return CommonResponse.ok(productService.getProductListByswitch(param,userId));
    }
    @Operation(summary = "메인페이지 키득 BEST 상품 목록", description ="메인페이지 키득 BEST 상품 목록을 조회합니다.")
    @GetMapping("/get/keyduek-best")
    public CommonResponse<?> getKeydeukBest(@AuthenticationPrincipal PrincipalDetails principalDetails){
        /** 구매나 리뷰 완료되면 로직 재구성 */
        Long userId = null;
        if (principalDetails!= null){
            userId = principalDetails.getUserId();
        }

        return CommonResponse.ok(productService.getBestProductList(userId));
    }

}
