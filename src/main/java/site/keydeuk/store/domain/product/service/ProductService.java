package site.keydeuk.store.domain.product.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.customoption.dto.OptionProductsResponseDto;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.product.dto.productdetail.ProductDetailResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListResponseDto;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.product.specification.ProductSpecification;
import site.keydeuk.store.entity.Product;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final LikesService likesService;
    private final EntityManager entityManager;

    /** 상품 상세 조회 */
    public ProductDetailResponseDto getProductDetailById(Integer productId){

        Product product = getProductById(productId);

        // 조회수 증가
        product.setViews(product.getViews()+1);
        productRepository.save(product);

        return new ProductDetailResponseDto(product);
    }


    /** 전체 상품 조회 */
    public Page<ProductListResponseDto>  getProductAllList(String sort, Pageable pageable,Long userId) {
        Page<Product> products;

        // 정렬 방식에 따라 페이지 정렬 설정
        switch (sort) {
            case "createdAt_desc":
                products = productRepository.findAllByOrderByCreatedAtDesc(pageable);
                break;
            case "views_desc":
                products = productRepository.findAllByOrderByViewsDesc(pageable);
                break;
            case "price_asc":
                products = productRepository.findAllByOrderByPriceAsc(pageable);
                break;
            case "price_desc":
                products = productRepository.findAllByOrderByPriceDesc(pageable);
                break;
            default:
                products = productRepository.findAllOrderByOrder(pageable); //defalut :  주문 많은 순
        }
        return products.map(product -> {
            boolean isLiked = false;
            if (userId != null) {
                isLiked = likesService.existsByUserIdAndProductId(userId, product.getId());
            }
            return new ProductListResponseDto(product,isLiked);
        });
    }


    /**카테고리 별 상품 조회*/
    public Page<ProductListResponseDto> getProductListByCategory(String category, List<String> companies, List<String> switchOptions,
                                                  int minPrice, int maxPrice,String sort,Pageable pageable, Long userId){
        Specification<Product> spec = Specification.where(ProductSpecification.categoryEquals(category));

        if (companies != null && !companies.isEmpty()) {
            spec = spec.and(ProductSpecification.companyIn(companies));
        }

        if (switchOptions != null && !switchOptions.isEmpty()) {
            spec = spec.and(ProductSpecification.switchOptionsIn(switchOptions));
        }

        if (minPrice >= 0 && maxPrice >= 0) {
            spec = spec.and(ProductSpecification.priceBetween(minPrice, maxPrice));
        }

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
            default:
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
                break;
        }
        Page<Product> products = productRepository.findAll(spec,pageable);

        return products.map(product -> {
            boolean isLiked = false;
            if (userId != null) {
                isLiked = likesService.existsByUserIdAndProductId(userId, product.getId());
            }
            return new ProductListResponseDto(product,isLiked);
        });
    }

    /**카테고리 별 인기 상품 조회*/
    public Page<ProductListResponseDto> getProductListByCategoryOrderByPopular(String category, List<String> companies, List<String> switchOptions
            , int minPrice, int maxPrice,Pageable pageable,Long userId){
        List<Product> products;
        if (category.equals("etc")){
            products = productRepository.findProductsByETCOrderedByOrderCountAndFiltered(companies,switchOptions,minPrice,maxPrice);
            log.info("size: {}",products.size());
        }else {
            products = productRepository.findProductsOrderedByOrderCountAndFiltered(category,companies,switchOptions,minPrice,maxPrice);
        }

        List<ProductListResponseDto> dtos = products.stream().map(product -> {
            boolean isLiked = false;
            if (userId != null) isLiked = likesService.existsByUserIdAndProductId(userId,product.getId());
            return new ProductListResponseDto(product,isLiked);
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos,pageable,dtos.size());
    }



    /** 옵션 상품 목록 */
    public List<OptionProductsResponseDto> getOptionProductList(){
        List<OptionProductsResponseDto> productlist = new ArrayList<>();

        for (int i=4;i<=8; i++){
            productlist.add(getRandomOptionProduct(i));
        }
        return productlist;
    }

    /** 키득pick 스위치로 검색  */
    public List<ProductListResponseDto> getProductListByswitch(String param, Long userId){
        if (param.equals("가성비")){
            return getRandomProductListForPick(productRepository.findByProductCategoryIdAndPriceLessThanEqual(1),4,userId);
        }else {
            return getRandomProductListForPick(productRepository.findByProductCategoryIdAndSwitchOptions_OptionName(1,param),4,userId);
        }
    }

    /** 키득BEST 구매순 20위까지 조회*/
    public List<ProductListResponseDto> getBestProductList(Long userId){

        //return getRandomProductListForPick(productRepository.findByProductCategoryId(1),20,userId);
        return getRandomProductListForPick(productRepository.findOrderByOrderedMostByCategory(1),20,userId);
    }

    private List<ProductListResponseDto> getRandomProductListForPick(List<Product> products,int size, Long userId){

        List<ProductListResponseDto> dtos = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i <size ; i++) {
            int randomIndex = random.nextInt(products.size());
            boolean isLiked = false;
            if (userId != null) {
                isLiked = likesService.existsByUserIdAndProductId(userId, products.get(randomIndex).getId());
            }
            ProductListResponseDto dto = new ProductListResponseDto(products.get(randomIndex),isLiked);
            dtos.add(dto);
        }
        return dtos;
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
