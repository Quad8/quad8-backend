package site.keydeuk.store.domain.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.common.security.authentication.dto.AuthenticationToken;
import site.keydeuk.store.domain.security.dto.ReissueRequest;
import site.keydeuk.store.domain.security.service.SecurityService;

@Tag(name = "Security", description = "Security 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @Operation(summary = "토큰 재발급", description = "액세스 토큰과 리프레시 토큰을 재발급합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = AuthenticationToken.class)))
    @PostMapping("/reissue")
    public CommonResponse<AuthenticationToken> reissue(
            @Parameter(description = "토큰 재발급 요청 데이터", required = true, content = @Content(schema = @Schema(implementation = ReissueRequest.class)))
            @RequestBody ReissueRequest reissueRequest) {
        AuthenticationToken token = securityService.reissue(reissueRequest);
        return CommonResponse.ok(token);
    }
}