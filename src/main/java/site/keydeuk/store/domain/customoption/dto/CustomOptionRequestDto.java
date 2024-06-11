package site.keydeuk.store.domain.customoption.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomOptionRequestDto {

    @NotNull
    @Schema(description = "배열(full, tenkeyless)", example = "full")
    private String layout;

    @NotNull
    @Schema(description = "외관 재질(metal, plastic)", example = "metal")
    private String appearanceTexture;

    @NotNull
    @Schema(description = "외관 색", example = "#03fcbe")
    private String appearanceColor;

    @NotNull
    @Schema(description = "스위치(청축, 적축, 갈축, 흑축)", example = "청축")
    private String keyboardSwitch;

    @NotNull
    @Schema(description = "포인트 키캡 선택 여부(Y or N)", example = "Y")
    private String hasPointKey;

    @NotNull
    @Schema(description = "포인트 키캡 개수(필요한가??)", example = "10")
    private int keyCount;

    @NotNull
    @Schema(description = "커스텀 키보드 이미지", example = "이미지!!")
    private String imgUrl;

    @NotNull
    @Schema(description = "가격", example = "99000")
    private int price;

}
