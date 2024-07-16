package site.keydeuk.store.domain.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.coupon.dto.request.CreateCouponRequest;
import site.keydeuk.store.domain.coupon.dto.response.CouponResponse;
import site.keydeuk.store.domain.coupon.service.CouponService;
import site.keydeuk.store.domain.security.PrincipalDetails;

import java.util.List;

@Tag(name = "Coupon", description = "쿠폰 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CouponController {
    private final CouponService couponService;

    @Operation(summary = "쿠폰 생성 및 저장", description = "쿠폰을 생성하고 저장합니다.")
    @PostMapping("/coupon/create")
    public CommonResponse<CouponResponse> confirm(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody CreateCouponRequest request
    ) {
        Long userId = principalDetails.getUserId();
        CouponResponse confirm = couponService.createCoupon(userId, request);
        return CommonResponse.ok(confirm);
    }

    @GetMapping("/user/coupon")
    public CommonResponse<List<CouponResponse>> getCouponsByUserId(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Long userId = principalDetails.getUserId();
        List<CouponResponse> coupons = couponService.findCouponsByUserId(userId);
        return CommonResponse.ok(coupons);
    }
}
