package site.keydeuk.store.domain.security.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 재발급 요청 데이터")
public record ReissueRequest(
        @Schema(description = "리프레시 토큰", example = "dGhpcyBpcyByZWZyZXNoIHRva2VuLg==")
        String refreshToken,
        @Schema(description = "만료된 Access 토큰", example = "dGhpcyBpcyByZWZyZXNoIHRva2VuLg==")
        String accessToken
) {
}
