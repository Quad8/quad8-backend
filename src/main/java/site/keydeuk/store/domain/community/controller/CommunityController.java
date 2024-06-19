package site.keydeuk.store.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.community.dto.create.PostDto;
import site.keydeuk.store.domain.community.service.CommunityService;
import site.keydeuk.store.domain.security.PrincipalDetails;

import java.util.List;

@Slf4j
@Tag(name ="Community", description = "커뮤니티 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @Operation(summary = "(미구현!!!!)커스텀키보드 구매내역 조회", description = "커스텀 키보드 구매내역을 조회합니다.")
    @GetMapping("/get/purchase-history")
    public CommonResponse<?> getPurchaseHistory(@AuthenticationPrincipal PrincipalDetails principalDetails){

        return CommonResponse.ok();
    }

    @Operation(summary = "커뮤니티 글 작성하기", description = "커스텀 키보드 구매 내역 확인 후 글 작성이 가능합니다. ")
    @PostMapping("/create")
    public CommonResponse<?> createPost(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @Validated @RequestPart("postDto")PostDto postDto,
                                        @RequestPart(value = "files")@Parameter(description = "이미지 파일")List<MultipartFile> files){
        if (files.size() >4){
            return CommonResponse.error("파일은 최대 4장까지 가능합니다.");
        }
        return CommonResponse.ok("글 저장되었습니다.",communityService.createPost(principalDetails.getUserId(),postDto,files));
    }

    @Operation(summary = "커뮤니티 글 삭제하기", description = "작성된 글을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public CommonResponse<?> deletePost(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("id")Long id){
        // user가 쓴 게시글 맞는지 확인
        if (communityService.findPostByuserIdAndCommunityId(principalDetails.getUserId(), id) == null) return CommonResponse.error("작성한 게시글이 아닙니다.");

        communityService.deletePost(id);

        return CommonResponse.ok("게시글이 삭제되었습니다.");
    }

}
