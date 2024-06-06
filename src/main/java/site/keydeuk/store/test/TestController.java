package site.keydeuk.store.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Test", description = "Test 관련 API 입니다.")
@RequestMapping("/api/v1/test")
@RestController
public class TestController {

    @Autowired
    private UserTestRepository userTestRepository;

    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserTestDto dto){

        UserTestEntity entity = UserTestEntity.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .birth(dto.getBirth())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .role(dto.getRole())
                .status(dto.getStatus())
                .nickname(dto.getNickname())
                .imgUrl(dto.getImgUrl())
                .provider(dto.getProvider())
                .providerId(dto.getProviderId())
        .build();

        UserTestEntity registeredUser = userTestRepository.save(entity);

        return ResponseEntity.ok(registeredUser);

    }

    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserTestResponseDto.class)))
    @Operation(summary = "회원 조회", description = "id로 회원을 조회합니다.")
    @Parameter(name = "id", description = "회원 ID" ,example = "107")
    @GetMapping("/userInfo/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable("id")Integer id){

        UserTestEntity entity = userTestRepository.getById(id);
        UserTestResponseDto dto= new UserTestResponseDto();
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setNickname(entity.getNickname());

        return ResponseEntity.ok(dto);
    }
}
