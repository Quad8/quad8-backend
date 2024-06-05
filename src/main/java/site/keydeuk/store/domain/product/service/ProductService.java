package site.keydeuk.store.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.product.dto.productlist.ProductListAndTotalResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListResponseDto;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /** 전체 상품 조회 */
    public ProductListAndTotalResponseDto getProductAllList(){
        List<Product> products = productRepository.findAll();

        // 리뷰수 -- 미구현

        List<ProductListResponseDto> list =  products.stream()
                .map(ProductListResponseDto::new)
                .collect(Collectors.toList());

        return new ProductListAndTotalResponseDto(list.size(), list);
    }

    public ProductListAndTotalResponseDto getProductListByCategory(Integer categoryId){

        List<Product> products = new ArrayList<>();

        if (categoryId<4){
            products = productRepository.findByProductCategoryId(categoryId);
        }else {
            products = productRepository.findProductsByCategoryIdBetween(4,8);
        }

        // 리뷰수 -- 미구현

        List<ProductListResponseDto> list = products.stream()
                .map(ProductListResponseDto::new)
                .collect(Collectors.toList());

        return new ProductListAndTotalResponseDto(list.size(), list);
    }
}
