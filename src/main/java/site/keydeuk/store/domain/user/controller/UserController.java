package site.keydeuk.store.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.cart.service.CartService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.user.dto.request.JoinRequest;
import site.keydeuk.store.domain.user.dto.request.UpdateProfileRequest;
import site.keydeuk.store.domain.user.dto.response.UserResponse;
import site.keydeuk.store.domain.user.service.UserService;
import site.keydeuk.store.entity.User;

@Slf4j
@Tag(name = "User", description = "User 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final CartService cartService;

    @Operation(summary = "회원가입", description = "회원가입을 위한 api 입니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Long> join(
            @Validated @RequestPart("joinRequest") JoinRequest joinRequest,
            @RequestPart(value = "imgFile", required = false) @Parameter(description = "프로필 이미지 파일") MultipartFile imgFile) {
        Long userId = userService.join(joinRequest, imgFile);

        // 회원 가입 success -> cart 생성
        cartService.createCart(userId);
        return CommonResponse.ok(userId);
    }

    @Operation(summary = "이메일 중복 확인", description = "주어진 이메일이 시스템에 이미 존재하는지 확인합니다.")
    @GetMapping("/check/email")
    public CommonResponse<Boolean> duplicateEmail(
            @Parameter(description = "확인할 이메일 주소", example = "user@example.com") @RequestParam String email) {
        boolean response = userService.isExistEmail(email);
        return CommonResponse.ok(response);
    }

    @Operation(summary = "닉네임 중복 확인", description = "주어진 닉네임이 시스템에 이미 존재하는지 확인합니다.")
    @GetMapping("/check/nickname")
    public CommonResponse<Boolean> duplicateNickname(
            @Parameter(description = "확인할 닉네임", example = "nickname123") @RequestParam String nickname) {
        boolean response = userService.isExistNickname(nickname);
        return CommonResponse.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "로그인된 사용자의 정보를 반환합니다.")
    public CommonResponse<UserResponse> getMyInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        User user = userService.findById(principalDetails.getUserId());
        return CommonResponse.ok(UserResponse.from(user));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "타인 정보 조회", description = "클릭한 사용자의 정보를 반환합니다.")
    public CommonResponse<UserResponse> getUserInfo(
            @PathVariable Long userId
    ) {
        User user = userService.findById(userId);
        return CommonResponse.ok(UserResponse.from(user));
    }

    @PutMapping(path = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "내 정보 수정", description = "사용자 프로필 정보를 수정합니다.")
    public CommonResponse<Void> updateProfile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestPart("updateProfileRequest") @Validated UpdateProfileRequest updateProfileRequest,
            @RequestPart(value = "imgFile", required = false) @Parameter(description = "프로필 이미지 파일") MultipartFile imgFile
    ) {
        Long userId = principalDetails.getUserId();
        userService.updateProfile(userId, updateProfileRequest, imgFile);
        return CommonResponse.ok();
    }

}
