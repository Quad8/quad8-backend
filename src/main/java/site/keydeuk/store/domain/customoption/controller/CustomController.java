package site.keydeuk.store.domain.customoption.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.customoption.dto.CustomOptionRequestDto;
import site.keydeuk.store.domain.customoption.service.CustomService;

@Slf4j
@Tag(name = "Custom", description = "커스텀 키보드 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/custom")
@RestController
public class CustomController {

    private final CustomService customService;
    @Operation(summary = "커스텀 키보드 생성",description = "조합한 커스텀 키보드를 저장합니다.")
    @PostMapping("/create/custom-keyboard")
    public CommonResponse<?> createCustomKeyboard(@Valid CustomOptionRequestDto requestDto){

    }
}
