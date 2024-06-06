package site.keydeuk.store.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.product.dto.productdetail.ProductDetailResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListAndTotalResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListRequestDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListResponseDto;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.entity.Product;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /** 상품 상세 조회 */
    public ProductDetailResponseDto getProductDetailById(Integer productId){

        Product product = getProductById(productId);

        return new ProductDetailResponseDto(product);
    }


    /** 전체 상품 조회 */
    public ProductListAndTotalResponseDto getProductAllList(String sort){
        List<Product> products = productRepository.findAll();

        // 리뷰수 -- 미구현

        return createResponseDto(sortProductList(sort,products));
    }


    /**카테고리 별 상품 조회*/
    public ProductListAndTotalResponseDto getProductListByCategory(Integer categoryId,String sort){

        List<Product> products;

        if (categoryId<4){
            products = productRepository.findByProductCategoryId(categoryId);
        }else {
            products = productRepository.findProductsByCategoryIdBetween(4,8);
        }

        // 리뷰수 -- 미구현

        return createResponseDto(sortProductList(sort,products));

    }

    private Product getProductById(Integer productId){
        return productRepository.findById(productId).orElseThrow(
                ()-> new NoSuchElementException("Product not found with id: " + productId)
        );
    }

    /** 정렬 */
    private List<Product> sortProductList(String sort, List<Product> products){

        switch (sort){
            case "createdAt_desc":
                products.sort((Comparator.comparing(Product::getCreatedAt).reversed()));
                break;
            case "views_desc":
                products.sort((Comparator.comparing(Product::getViews).reversed()));
                break;
            case "price_asc": //가격 낮은순
                products.sort((Comparator.comparing(Product::getPrice)));
                break;
            case "price_desc":
                products.sort((Comparator.comparing(Product::getPrice).reversed()));
                break;
            /** 인기순 -> default로 구현 필요*/
        }
        return products;
    }

    /** responseDto 생성*/
    private ProductListAndTotalResponseDto createResponseDto(List<Product> products){

        List<ProductListResponseDto> list =  products.stream()
                .map(ProductListResponseDto::new)
                .collect(Collectors.toList());

        return new ProductListAndTotalResponseDto(list.size(), list);
    }
}
