package site.keydeuk.store.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.user.dto.request.JoinRequest;
import site.keydeuk.store.domain.user.service.UserService;

@Tag(name = "User", description = "User 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입을 위한 api 입니다.")
    @PostMapping
    public CommonResponse<Long> join(@RequestBody @Validated JoinRequest joinRequest) {
        Long userId = userService.join(joinRequest);
        return CommonResponse.ok(userId);
    }

    @Operation(summary = "이메일 중복 확인", description = "주어진 이메일이 시스템에 이미 존재하는지 확인합니다.")
    @GetMapping("/check/email")
    public CommonResponse<Boolean> duplicateEmail(@Parameter(description = "확인할 이메일 주소", example = "user@example.com") @RequestParam String email) {
        boolean response = userService.isExistEmail(email);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "닉네임 중복 확인", description = "주어진 닉네임이 시스템에 이미 존재하는지 확인합니다.")
    @GetMapping("/check/nickname")
    public CommonResponse<Boolean> duplicateNickname(@Parameter(description = "확인할 닉네임", example = "nickname123") @RequestParam String nickname) {
        boolean response = userService.isExistNickname(nickname);
        return CommonResponse.ok(response);
    }

}
