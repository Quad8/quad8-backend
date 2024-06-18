package site.keydeuk.store.domain.product.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.customoption.dto.OptionProductsResponseDto;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.product.dto.productdetail.ProductDetailResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListResponseDto;
import site.keydeuk.store.domain.product.repository.ProductRepository;
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

        return products.map(product -> {
            boolean isLiked = false;
            if (userId != null) {
                isLiked = likesService.existsByUserIdAndProductId(userId, product.getId());
            }
            return new ProductListResponseDto(product,isLiked);
        });
    }


    /**카테고리 별 상품 조회*/
    public Page<ProductListResponseDto> getProductListByCategory(Integer categoryId,String sort,Pageable pageable, Long userId){

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
        return products.map(product -> {
            boolean isLiked = false;
            if (userId != null) {
                isLiked = likesService.existsByUserIdAndProductId(userId, product.getId());
            }
            return new ProductListResponseDto(product,isLiked);
        });
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

        return getRandomProductListForPick(productRepository.findByProductCategoryId(1),20,userId);
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


    public Page<ProductListResponseDto> getProductListByCategoryAndFilters(
            Integer startCategoryId, Integer endCategoryId,
            String company, Integer minPrice, Integer maxPrice,
            String sort, Pageable pageable,Long userId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        // 조건을 담을 리스트
        List<Predicate> predicates = new ArrayList<>();

        // 카테고리 범위 조건
        predicates.add(cb.between(root.get("productCategory").get("id"), startCategoryId, endCategoryId));

        // 제조사 조건
        if (company != null && !company.isEmpty()) {
            predicates.add(cb.equal(root.get("company"), company));
        }

        // 가격 범위 조건
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        // 조건들을 AND로 결합
        cq.where(predicates.toArray(new Predicate[0]));

        // 정렬 조건 추가
        switch (sort) {
            case "createdAt_desc":
                cq.orderBy(cb.desc(root.get("createdAt")));
                break;
            case "views_desc":
                cq.orderBy(cb.desc(root.get("views")));
                break;
            case "price_asc":
                cq.orderBy(cb.asc(root.get("price")));
                break;
            case "price_desc":
                cq.orderBy(cb.desc(root.get("price")));
                break;
            // 기본 정렬 설정 필요
        }

        // 쿼리 실행 및 페이징 처리
        List<Product> resultList = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = getTotalCount(startCategoryId, endCategoryId, company, minPrice, maxPrice);

        List<ProductListResponseDto> dtos = resultList.stream().map(product -> {
           boolean isLiked = false;
           if (userId != null) isLiked = likesService.existsByUserIdAndProductId(userId,product.getId());
           return new ProductListResponseDto(product,isLiked);
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, total);
    }

    private long getTotalCount(Integer startCategoryId, Integer endCategoryId, String company, Integer minPrice, Integer maxPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.between(countRoot.get("productCategory").get("id"), startCategoryId, endCategoryId));

        if (company != null && !company.isEmpty()) {
            countPredicates.add(cb.equal(countRoot.get("company"), company));
        }

        if (minPrice != null) {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("price"), minPrice));
        }

        if (maxPrice != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("price"), maxPrice));
        }

        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }


}
