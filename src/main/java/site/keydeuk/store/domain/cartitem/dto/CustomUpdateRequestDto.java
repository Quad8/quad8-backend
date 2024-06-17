package site.keydeuk.store.domain.cartitem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUpdateRequestDto {
    @NotNull
    @Schema(description = "배열(full, tenkeyless)", example = "full")
    private String type;

    @NotNull
    @Schema(description = "외관 재질(metal, plastic)", example = "metal")
    private String texture;

    @NotNull
    @Schema(description = "boardColor", example = "#03fcbe")
    private String boardColor;

    @NotNull
    @Schema(description = "스위치(청축, 적축, 갈축, 흑축)", example = "청축")
    private String switchType;

    @NotNull
    @Schema(description = "포인트 키캡 선택 여부(true or false)", example = "true")
    private Boolean hasPointKeyCap;

    @Schema(description = "baseKeyColor", example = "#ffffff")
    private String baseKeyColor;

    @Schema(description = "pointKeyType", example = "내 맘대로 바꾸기")
    private String pointKeyType;

    @Schema(description = "pointSetColor", example = "#ffffff")
    private String pointSetColor;

    @NotNull
    @Schema(description = "가격", example = "99000")
    private int price;

    @Schema(description = "키캡 옵션")
    private Object individualColor;

    @NotNull
    @Schema(description = "커스텀 키보드 이미지", example = "base64형식 image")
    private String imgBase64;

}
