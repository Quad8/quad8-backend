package site.keydeuk.store.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.customoption.dto.OptionProductsResponseDto;
import site.keydeuk.store.domain.product.dto.productdetail.ProductDetailResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListResponseDto;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.entity.Product;

import java.util.*;
@Slf4j
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


    /** 옵션 상품 목록 */
    public List<OptionProductsResponseDto> getOptionProductList(){
        List<OptionProductsResponseDto> productlist = new ArrayList<>();

        for (int i=4;i<=8; i++){
            productlist.add(getRandomOptionProduct(i ));
        }
        return productlist;
    }

    private OptionProductsResponseDto getRandomOptionProduct(int categoryId){
        Random random = new Random();
        List<Product> productsIdBy4 =  productRepository.findByProductCategoryId(categoryId);
        int randomIndex = random.nextInt(productsIdBy4.size());
        log.info("index={}",randomIndex);
        return new OptionProductsResponseDto(productsIdBy4.get(randomIndex));
    }

    private Product getProductById(Integer productId){
        return productRepository.findById(productId).orElseThrow(
                ()-> new NoSuchElementException("Product not found with id: " + productId)
        );
    }

}
