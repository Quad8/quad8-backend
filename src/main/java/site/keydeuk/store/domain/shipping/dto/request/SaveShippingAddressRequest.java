package site.keydeuk.store.domain.shipping.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import site.keydeuk.store.entity.ShippingAddress;
import site.keydeuk.store.entity.User;

@Schema(description = "배송지 저장 요청 데이터")
public record SaveShippingAddressRequest(
        @Schema(description = "이름", example = "John Doe")
        @NotEmpty(message = "이름은 필수 입력 항목입니다.")
        String name,

        @Schema(description = "우편번호", example = "12345")
        @NotEmpty(message = "우편번호는 필수 입력 항목입니다.")
        String zoneCode,

        @Schema(description = "주소", example = "123 Main St")
        @NotEmpty(message = "주소는 필수 입력 항목입니다.")
        String address,

        @Schema(description = "상세 주소", example = "Apt 101")
        @NotEmpty(message = "상세주소는 필수 입력 항목입니다.")
        String detailAddress,

        @Schema(description = "전화번호", example = "01012345678")
        @NotEmpty(message = "전화번호는 필수 입력 항목입니다.")
        String phone,

        @Schema(description = "기본 배송지 여부", example = "true")
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
