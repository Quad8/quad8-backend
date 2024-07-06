package site.keydeuk.store.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.cart.dto.cartlist.CartByUserResponseDto;
import site.keydeuk.store.domain.cart.service.CartService;
import site.keydeuk.store.domain.cartitem.dto.CartItemReqeustDto;
import site.keydeuk.store.domain.cartitem.dto.CustomUpdateRequestDto;
import site.keydeuk.store.domain.cartitem.dto.delete.DeleteCartItemRequestDto;
import site.keydeuk.store.domain.cartitem.dto.update.ProductUpdateRequestDto;
import site.keydeuk.store.domain.cartitem.service.CartItemService;
import site.keydeuk.store.domain.security.PrincipalDetails;

@Slf4j
@Tag(name = "Cart", description = "장바구니 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@RestController
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    @Operation(summary = "장바구니 조회", description = "로그인한 유저의 장바구니 목록을 조회합니다.")
    @GetMapping("/get")
    public CommonResponse<?> getUserCartList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        CartByUserResponseDto dto = cartService.getCartByUserId(principalDetails.getUserId());

        return CommonResponse.ok(dto);
    }

    @Operation(summary = "장바구니 담기", description = "상품을 장바구니에 담습니다.")
    @PostMapping("/add")
    public CommonResponse<?> addProductToCart(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody CartItemReqeustDto reqeustDto){
        Long cartItemId = cartService.addProductToCart(reqeustDto,principalDetails.getUserId());

        return CommonResponse.ok(cartItemId);
    }

    @Operation(summary = "장바구니 커스텀 키보드 수정", description = "장바구니에 담긴 커스텀 키보드 옵션을 수정합니다.")
    @PutMapping("/update/custom/{id}")
    public CommonResponse<?> updateCustomToCart(@PathVariable("id")Long id, @RequestBody CustomUpdateRequestDto requestDto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        cartItemService.updateCustom(id,requestDto);

        return CommonResponse.ok("장바구니 수정 완료",null);
    }

    @Operation(summary = "장바구니 상품 수정", description = "장바구니에 담긴 상품 수량 및 옵션을 수정합니다.")
    @PutMapping("/update/product/{id}")
    public CommonResponse<?> updateProductToCart(@PathVariable("id")Long id, @RequestBody ProductUpdateRequestDto requestDto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        cartItemService.updateProduct(id, requestDto);

        return CommonResponse.ok("장바구니 수정 완료", null);
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 상품을 삭제합니다.")
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteProductToCart(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody DeleteCartItemRequestDto requestDto){
            cartItemService.delete(requestDto,principalDetails.getUserId());
            return CommonResponse.ok();
    }
}
