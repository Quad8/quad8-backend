package site.keydeuk.store.domain.customoption.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.cart.service.CartService;
import site.keydeuk.store.domain.cartitem.dto.CartItemReqeustDto;
import site.keydeuk.store.domain.customoption.dto.custom.CustomKeyboardRequestDto;
import site.keydeuk.store.domain.customoption.service.CustomService;
import site.keydeuk.store.domain.product.service.ProductService;
import site.keydeuk.store.domain.security.PrincipalDetails;

@Slf4j
@Tag(name = "Custom", description = "커스텀 키보드 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/custom")
@RestController
public class CustomController {

    private final CustomService customService;
    private final ProductService productService;
    private final CartService cartService;

    @Operation(summary = "커스텀 키보드 주문",description = "스웨거XXXXX, 조합한 커스텀 키보드를 장바구니에 저장합니다.")
    @PostMapping("/create")
    public CommonResponse<?> createCustomKeyboard(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid CustomKeyboardRequestDto requestDto){

        // 1. 커스텀 키보드 옵션 저장
        Integer productId = customService.saveCustomOption(requestDto);
        log.info("productId: {}",productId);

        // 2. 장바구니 저장
        Long cartItemId = cartService.addCustomToCart(CartItemReqeustDto.builder()
                .productId(productId).count(1).build(),principalDetails.getUserId());
        // 2-1 옵션 상품 장바구니 저장
        if(!requestDto.getOption().isEmpty()){
            for (Integer id : requestDto.getOption()){
                log.info("option-productId: {}",id);

                cartService.addProductToCart(CartItemReqeustDto.builder()
                        .productId(id).count(1).build(),principalDetails.getUserId());
            }
        }
        return CommonResponse.ok("장바구니에 저장 완료",cartItemId);
    }
    @Operation(summary = "옵셥 상품 목록", description = "카테고리 별 옵션 상품 5개을 보여줍니다.")
    @GetMapping("/get/random-option-products")
    public CommonResponse<?> getRandomOptionProducts(){

        return CommonResponse.ok(productService.getOptionProductList());
    }
}
