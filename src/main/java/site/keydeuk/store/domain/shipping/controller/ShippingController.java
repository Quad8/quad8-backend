package site.keydeuk.store.domain.shipping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.shipping.dto.request.SaveShippingAddressRequest;
import site.keydeuk.store.domain.shipping.dto.response.ShippingAddressResponse;
import site.keydeuk.store.domain.shipping.service.ShippingService;

@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/address")
    public CommonResponse<ShippingAddressResponse> saveShippingAddress(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Validated SaveShippingAddressRequest saveShippingAddressRequest
    ) {
        ShippingAddressResponse response = shippingService.saveShippingAddress(principalDetails, saveShippingAddressRequest);
        return CommonResponse.ok(response);
    }
}
