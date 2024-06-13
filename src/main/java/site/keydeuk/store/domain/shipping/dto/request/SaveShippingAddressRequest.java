package site.keydeuk.store.domain.shipping.dto.request;

import site.keydeuk.store.entity.ShippingAddress;
import site.keydeuk.store.entity.User;

public record SaveShippingAddressRequest(
        String name,
        String zoneCode,
        String address,
        String detailAddress,
        String phone,
        Boolean isDefault
) {
    public ShippingAddress toEntity(User user, SaveShippingAddressRequest request) {
        return ShippingAddress.builder()
                .name(request.name)
                .zoneCode(request.zoneCode)
                .address(request.address)
                .detailAddress(request.detailAddress())
                .phone(request.phone)
                .isDefault(request.isDefault)
                .user(user)
                .build();
    }
}
