package site.keydeuk.store.domain.shipping.dto;

import lombok.Builder;
import site.keydeuk.store.entity.ShippingAddress;

@Builder
public record ShippingAddressDto(
        Long id,
        String name,
        String zoneCode,
        String address,
        String detailAddress,
        String phone
) {
    public static ShippingAddressDto from(ShippingAddress shippingAddress) {
        return ShippingAddressDto.builder()
                .id(shippingAddress.getId())
                .name(shippingAddress.getName())
                .zoneCode(shippingAddress.getZoneCode())
                .address(shippingAddress.getAddress())
                .detailAddress(shippingAddress.getDetailAddress())
                .phone(shippingAddress.getPhone())
                .build();
    }
}
