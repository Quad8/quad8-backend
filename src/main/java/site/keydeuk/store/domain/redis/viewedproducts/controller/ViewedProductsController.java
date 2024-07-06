package site.keydeuk.store.domain.redis.viewedproducts.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.security.PrincipalDetails;

@Slf4j
@Tag(name = "최근 본 상품 목록", description = "최근 본 상품 목록 조회 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class ViewedProductsController {

    public CommonResponse<?> getRecentlyViewedProducts(@AuthenticationPrincipal PrincipalDetails principalDetails){



        return CommonResponse.ok();
    }
}
