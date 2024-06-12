package site.keydeuk.store.domain.shipping.dto.request;

public record SaveShippingAddressRequest(
        String name,
        String zoneCode,
        String address,
        String detailAddress,
        String phone,
        Boolean isDefault
) {

}
