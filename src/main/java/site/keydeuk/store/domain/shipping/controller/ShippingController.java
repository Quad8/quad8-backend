package site.keydeuk.store.domain.shipping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.shipping.dto.request.SaveShippingAddressRequest;
import site.keydeuk.store.domain.shipping.dto.request.UpdateShippingAddressRequest;
import site.keydeuk.store.domain.shipping.dto.response.ShippingAddressResponse;
import site.keydeuk.store.domain.shipping.service.ShippingService;

import java.util.List;

@Tag(name = "Shipping", description = "배송 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/address")
    @Operation(summary = "배송지 저장", description = "새로운 배송지 정보를 저장합니다.")
    public CommonResponse<ShippingAddressResponse> saveShippingAddress(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody SaveShippingAddressRequest saveShippingAddressRequest
    ) {
        ShippingAddressResponse response = shippingService.saveShippingAddress(principalDetails, saveShippingAddressRequest);
        return CommonResponse.ok(response);
    }

    @GetMapping("/address")
    @Operation(summary = "배송지 조회", description = "등록된 모든 배송지 정보를 조회합니다.")
    public CommonResponse<List<ShippingAddressResponse>> getShippingAddress(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        List<ShippingAddressResponse> shippingAddresses = shippingService.getShippingAddresses(principalDetails.getUserId());
        return CommonResponse.ok(shippingAddresses);
    }

    @PutMapping("/address/{addressId}")
    @Operation(summary = "배송지 수정", description = "배송지 정보를 수정합니다.")
    public CommonResponse<ShippingAddressResponse> modifyShippingAddress(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long addressId,
            @RequestBody @Validated @ParameterObject UpdateShippingAddressRequest request
    ) {
        Long userId = principalDetails.getUserId();
        ShippingAddressResponse response = shippingService.updateShippingAddress(userId, addressId, request);
        return CommonResponse.ok(response);
    }

    @DeleteMapping("/address/{addressId}")
    @Operation(summary = "배송지 삭제", description = "배송지 정보를 삭제합니다.")
    public CommonResponse<Void> deleteShippingAddress(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long addressId
    ) {
        Long userId = principalDetails.getUserId();
        shippingService.deleteShippingAddress(userId, addressId);
        return CommonResponse.ok();
    }
}
