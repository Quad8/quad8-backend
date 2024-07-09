package site.keydeuk.store.domain.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.search.dto.ProductResponseDto;
import site.keydeuk.store.domain.search.service.SearchService;
import site.keydeuk.store.domain.security.PrincipalDetails;

import static site.keydeuk.store.common.response.ErrorCode.INVALID_PAGEABLE_PAGE;

@Slf4j
@Tag(name = "Search", description = "검색 관련 API 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "검색창 검색", description = "검색창에 입력된 검색어를 포함한 삼품 목록을 조회합니다.")
    @GetMapping()
    public CommonResponse<?> getSearchedProducts(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                 @RequestParam @NotBlank @Size(min = 1,max = 100) String search,
                                                 @RequestParam(defaultValue = "10") @NotNull int size,
                                                 @RequestParam(defaultValue = "0") @NotNull int page){
        Long userId = null;
        if (principalDetails!= null){
            userId = principalDetails.getUserId();
        }

        Pageable pageable = PageRequest.of(page,size);
        Page<ProductResponseDto> pages = searchService.searchProducts(search,pageable,userId);
        if (pages.getTotalPages() <= page) throw new CustomException(INVALID_PAGEABLE_PAGE);

        return CommonResponse.ok(pages);
    }

    @Operation(summary = "상품명 전체 목록", description = "검색어 자동 완성을 위한 상품명 전체 목록 조회")
    @GetMapping("all/products-name")
    public CommonResponse<?> getAllProductsName(){

        return CommonResponse.ok(searchService.getProductNames());
    }
}
