package site.keydeuk.store.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.cart.service.CartService;
import site.keydeuk.store.domain.cartitem.dto.CartItemReqeustDto;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.User;

@Slf4j
@Tag(name = "Cart", description = "장바구니 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@RestController
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 담기", description = "상품을 장바구니에 담습니다.")
    @PostMapping("/add")
    public CommonResponse<?> addProductToCart(@AuthenticationPrincipal PrincipalDetails principalDetails, @ParameterObject @RequestBody CartItemReqeustDto reqeustDto){
        Long cartItemId = cartService.addCart(reqeustDto,principalDetails.getUserId());

        return CommonResponse.ok(cartItemId);
    }
}
