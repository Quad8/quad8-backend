package site.keydeuk.store.domain.product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.product.service.ProductService;
@Slf4j
@Tag(name = "Product", description = "Product 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list/{keyword}")
    public CommonResponse<?> getProductList(@PathVariable("keyword") String word){
        //all : 전체, 1 keyboard, 2 keycap, 3 switch, 4-8 others

        //log.info("keyword = {}",word);

        if (word.equals("all")){
            return CommonResponse.ok(productService.getProductAllList());
        } else if (word.equals("keyboard")) {
            return CommonResponse.ok(productService.getProductListByCategory(1));
        }else if (word.equals("keycap")) {
            return CommonResponse.ok(productService.getProductListByCategory(2));
        }else if (word.equals("switch")) {
            return CommonResponse.ok(productService.getProductListByCategory(3));
        }else if (word.equals("others")) {
            return CommonResponse.ok(productService.getProductListByCategory(4));
        }

        return CommonResponse.fail("server 에러");
    }

}
