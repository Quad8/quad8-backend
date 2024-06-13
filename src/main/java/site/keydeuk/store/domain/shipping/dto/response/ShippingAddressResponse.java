package site.keydeuk.store.domain.shipping.dto.response;

import lombok.Builder;
import site.keydeuk.store.entity.ShippingAddress;
import site.keydeuk.store.entity.User;

@Builder
public record ShippingAddressResponse (
        String name,
        String zoneCode,
        String address,
        String detailAddress,
        String phone,
        Boolean isDefault
){
    public static ShippingAddressResponse from(ShippingAddress shippingAddress) {
        return ShippingAddressResponse.builder()
                .name(shippingAddress.getName())
                .zoneCode(shippingAddress.getZoneCode())
                .address(shippingAddress.getAddress())
                .detailAddress(shippingAddress.getDetailAddress())
                .phone(shippingAddress.getPhone())
                .isDefault(shippingAddress.getIsDefault())
                .build();

    }
}
