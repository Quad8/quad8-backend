package site.keydeuk.store.domain.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.common.security.authentication.dto.AuthenticationToken;
import site.keydeuk.store.domain.security.dto.ReissueRequest;
import site.keydeuk.store.domain.security.service.SecurityService;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/reissue")
    public CommonResponse<AuthenticationToken> reissue(@RequestBody ReissueRequest reissueRequest) {
        AuthenticationToken token = securityService.reissue(reissueRequest);
        return CommonResponse.ok(token);
    }
}
