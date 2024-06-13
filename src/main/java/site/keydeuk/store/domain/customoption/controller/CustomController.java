package site.keydeuk.store.domain.customoption.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.customoption.dto.custom.CustomKeyboardRequestDto;
import site.keydeuk.store.domain.customoption.service.CustomService;
import site.keydeuk.store.domain.image.service.ImageService;

import java.util.Base64;

@Slf4j
@Tag(name = "Custom", description = "커스텀 키보드 관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/custom")
@RestController
public class CustomController {

    private final CustomService customService;

    private final ImageService imageService;

    @Operation(summary = "커스텀 키보드 주문",description = "조합한 커스텀 키보드를 저장합니다.")
    @PostMapping("/create")
    public CommonResponse<?> createCustomKeyboard(@RequestBody @Valid CustomKeyboardRequestDto requestDto){

        // 1. 커스텀 키보드 옵션 저장
        // 1-1. base^4 -> byte[]
        String base64Image = requestDto.getImgBase64();
        String base64Data = base64Image.replaceAll("^data:image/[a-zA-Z]+;base64,", "");

        byte[] imageByte = Base64.getDecoder().decode(base64Data);
        String objectUrl = imageService.uploadBase64ToImage(imageByte);

        // 2, color option 저장


        // 3. 장바구니 저장

        return CommonResponse.ok( requestDto);
    }
    @Operation(summary = "옵셥 상품 목록", description = "카테고리 별 옵션 상품 5개을 보여줍니다.")
    @GetMapping("/get/random-option-products")
    public CommonResponse<?> getRandomOptionProducts(){

        return CommonResponse.ok();
    }
}
