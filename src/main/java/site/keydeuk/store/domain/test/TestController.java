package site.keydeuk.store.domain.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    private UserTestRepository userTestRepository;


    @GetMapping("/test1")
    public String test(){
        System.out.println("check check dev");
        return "test!!!";
    }

    @PostMapping("/test/user/register")
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
    @GetMapping("/test/get/userInfo/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable Integer id){

        UserTestEntity entity = userTestRepository.getById(id);
        UserTestResponseDto dto= new UserTestResponseDto();
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setNickname(entity.getNickname());

        return ResponseEntity.ok(dto);
    }
}
