package site.keydeuk.store.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.cart.dto.cartlist.CartByUserResponseDto;
import site.keydeuk.store.domain.cart.dto.cartlist.CartItemListDto;
import site.keydeuk.store.domain.cart.repository.CartRepository;
import site.keydeuk.store.domain.cart.service.CartService;
import site.keydeuk.store.domain.cartitem.dto.CartCustomResponseDto;
import site.keydeuk.store.domain.cartitem.dto.CartItemReqeustDto;
import site.keydeuk.store.domain.cartitem.dto.CartProductResponseDto;
import site.keydeuk.store.domain.cartitem.repository.CartItemRepository;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.*;

import java.util.Optional;

@Slf4j
@Tag(name = "Cart", description = "장바구니 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@RestController
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomObjectRepository customObjectRepository;

    @Operation(summary = "장바구니 조회", description = "로그인한 유저의 장바구니 목록을 조회합니다.")
    @GetMapping("/get")
    private CommonResponse<?> getUserCartList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        CartByUserResponseDto dto = cartService.getCartByUserId(principalDetails.getUserId());

        return CommonResponse.ok(dto);
    }

    @Operation(summary = "장바구니 담기", description = "상품을 장바구니에 담습니다.")
    @PostMapping("/add")
    public CommonResponse<?> addProductToCart(@AuthenticationPrincipal PrincipalDetails principalDetails, @ParameterObject @RequestBody CartItemReqeustDto reqeustDto){
        Long cartItemId = cartService.addProductToCart(reqeustDto,principalDetails.getUserId());

        return CommonResponse.ok(cartItemId);
    }
}
