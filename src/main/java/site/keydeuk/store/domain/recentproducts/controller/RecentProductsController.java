package site.keydeuk.store.domain.recentproducts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.recentproducts.service.RecentProductsService;
import site.keydeuk.store.domain.security.PrincipalDetails;

@Slf4j
@Tag(name = "최근 본 상품 목록", description = "최근 본 상품 목록 조회 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/recent-products")
@RestController
public class RecentProductsController {

    private final RecentProductsService recentProductsService;

    @Operation(summary = "최근 본 상품 목록 조회", description = "유저가 최근 본 상품 목록(최대 8개)을 조회합니다.")
    @Parameter(name = "userId", description = "유저 ID", example = "167")
    @GetMapping("/{userId}")
    public CommonResponse<?> getRecentlyViewedProducts(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                       @PathVariable("userId")Long userId){

        return CommonResponse.ok(recentProductsService.getRecentProducts(userId));
    }

    @Operation(summary = "최근 본 상품 목록 저장", description = "유저가 최근 본 상품 목록에 저장합니다. 최대 7일까지만 저장됩니다.")
    @Parameter(name = "userId", description = "유저 ID", example = "167")
    @Parameter(name = "productId", description = "상품 ID", example = "1")
    @PostMapping("/{userId}")
    public CommonResponse<?> addProduct(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                       @PathVariable("userId")Long userId,  @RequestParam Integer productId){
        recentProductsService.addProduct(userId,productId);
        return CommonResponse.ok("저장되었습니다.",null);
    }
}
