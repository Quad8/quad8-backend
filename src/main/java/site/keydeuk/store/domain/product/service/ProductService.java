package site.keydeuk.store.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.product.dto.productdetail.ProductDetailResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListResponseDto;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.entity.Product;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /** 상품 상세 조회 */
    public ProductDetailResponseDto getProductDetailById(Integer productId){

        Product product = getProductById(productId);

        // 조회수 증가
        product.setViews(product.getViews()+1);
        productRepository.save(product);

        return new ProductDetailResponseDto(product);
    }


    /** 전체 상품 조회 */
    public Page<ProductListResponseDto>  getProductAllList(String sort, Pageable pageable) {
        Page<Product> products;

        // 정렬 방식에 따라 페이지 정렬 설정
        switch (sort) {
            case "createdAt_desc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
                break;
            case "views_desc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("views").descending());
                break;
            case "price_asc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").ascending());
                break;
            case "price_desc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());
                break;
            /** 인기순 -> default로 구현 필요*/
        }

        products = productRepository.findAll(pageable);

        return products.map(ProductListResponseDto::new);
    }


    /**카테고리 별 상품 조회*/
    public Page<ProductListResponseDto> getProductListByCategory(Integer categoryId,String sort,Pageable pageable){

        Page<Product> products;

        switch (sort) {
            case "createdAt_desc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
                break;
            case "views_desc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("views").descending());
                break;
            case "price_asc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").ascending());
                break;
            case "price_desc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());
                break;
            /** 인기순 -> default로 구현 필요*/
        }

        if (categoryId<4){
            products = productRepository.findByProductCategoryId(categoryId,pageable);
        }else {
            products = productRepository.findProductsByCategoryIdBetween(4,8,pageable);
        }

        // 리뷰수 -- 미구현

        return products.map(ProductListResponseDto::new);
    }

    private Product getProductById(Integer productId){
        return productRepository.findById(productId).orElseThrow(
                ()-> new NoSuchElementException("Product not found with id: " + productId)
        );
    }

    /** 정렬 */
//    private Page<Product> sortProductList(String sort, Page<Product> products){
//
//        switch (sort){
//            case "createdAt_desc":
//                products.sort((Comparator.comparing(Product::getCreatedAt).reversed()));
//                break;
//            case "views_desc":
//                products.sort((Comparator.comparing(Product::getViews).reversed()));
//                break;
//            case "price_asc": //가격 낮은순
//                products.sort((Comparator.comparing(Product::getPrice)));
//                break;
//            case "price_desc":
//                products.sort((Comparator.comparing(Product::getPrice).reversed()));
//                break;
//            /** 인기순 -> default로 구현 필요*/
//        }
//        return products;
//    }

}
