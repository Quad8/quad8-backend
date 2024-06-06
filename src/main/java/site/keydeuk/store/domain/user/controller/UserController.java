package site.keydeuk.store.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.user.dto.JoinRequest;
import site.keydeuk.store.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public CommonResponse<Long> join(@RequestBody @Validated JoinRequest joinRequest) {
        Long userId = userService.join(joinRequest);
        return CommonResponse.ok(userId);
    }


}
