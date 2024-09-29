package site.keydeuk.store.domain.product.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.redis.service.RedisService;
import site.keydeuk.store.domain.customoption.dto.OptionProductsResponseDto;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.product.dto.productdetail.ProductDetailResponseDto;
import site.keydeuk.store.domain.product.dto.productlist.ProductListResponseDto;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.product.specification.ProductSpecification;
import site.keydeuk.store.domain.review.service.ReviewService;
import site.keydeuk.store.entity.Product;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final LikesService likesService;
    private final ReviewService reviewService;
    private final RedisService redisService;
    private static final int MAX_CACHE_PRODUCT = 200;
    private static final long PRODUCT_EXPIRE_DAY = 604800;
    public static final String PRODUCT_KEY_PREFIX = "cache:product:";

    private Map<Integer,Integer> viewCount = new HashMap<>();

    /** 상품 상세 조회 */
    public ProductDetailResponseDto getProductDetailById(Integer productId){

        // 캐시에서 데이터 조회
        String key = PRODUCT_KEY_PREFIX+productId;
        Optional<ProductDetailResponseDto> optionalCacheDto = redisService.get(key, ProductDetailResponseDto.class);

        if (optionalCacheDto.isPresent()) {// cacheDto가 null이 아닐 때의 로직
            log.info("[info]: cache hit! productId: {}",key);

            //조회 수 증가
            incrementViewCountMap(productId);
            return optionalCacheDto.get();

        } else {
            // cacheDto가 null일 때의 로직
            Product product = getProductById(productId);

            // 조회 수 증가
            incrementViewCountMap(productId);

            Long reviewCount = reviewService.countByProductId(productId);
            Double scope = reviewService.getAverageScoreByProductId(productId);
            ProductDetailResponseDto responseDto = new ProductDetailResponseDto(product,reviewCount,scope);

            //캐시 저장, Sorted Set에 키 추가
            redisService.set(key, responseDto,PRODUCT_EXPIRE_DAY);
            redisService.addToSortedSet(PRODUCT_KEY_PREFIX,key);

            //200개 넘으면 오래된 것 순으로 삭제
            redisService.checkSizeAndRemoveKey(PRODUCT_KEY_PREFIX,MAX_CACHE_PRODUCT);
            return responseDto;
        }

    }

    private void incrementViewCountMap(Integer productId){
        viewCount.merge(productId, 1, Integer::sum);
    }

    @Transactional
    public void saveViewCount(){
        if (!viewCount.isEmpty()){
            for(Map.Entry<Integer, Integer> entry : viewCount.entrySet()){
                Integer productId = entry.getKey();
                Integer count = entry.getValue();

                Product product = getProductById(productId);
                product.setViews(product.getViews()+count);

                //캐시 삭제
                redisService.delete(PRODUCT_KEY_PREFIX+productId);
            }
            productRepository.saveAll(viewCount.keySet().stream().map(
                this::getProductById).collect(Collectors.toList()));

            log.info("[info] 누적된 조회수 DB 저장");
            viewCount.clear();
        }
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
            Long reviewCount =reviewService.countByProductId(product.getId());
            return new ProductListResponseDto(product,isLiked,reviewCount);
        });
    }


    /**카테고리 별 상품 조회*/
    public Page<ProductListResponseDto> getProductListByCategory(String category, List<String> companies, List<String> switchOptions,
                                                  long minPrice, long maxPrice,String sort,Pageable pageable, Long userId){
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
            Long reviewCount =reviewService.countByProductId(product.getId());
            return new ProductListResponseDto(product,isLiked,reviewCount);
        });
    }

    /**카테고리 별 인기 상품 조회*/
    public Page<ProductListResponseDto> getProductListByCategoryOrderByPopular(String category, List<String> companies, List<String> switchOptions
            , long minPrice, long maxPrice,Pageable pageable,Long userId){
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
            Long reviewCount =reviewService.countByProductId(product.getId());
            return new ProductListResponseDto(product,isLiked,reviewCount);
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

    /** 키득BEST 구매순 20위까지 조회*/
    public List<ProductListResponseDto> getBestProductList(Long userId){
        List<ProductListResponseDto> dtos = new ArrayList<>();
        List<Product> products = productRepository.findOrderByOrderedMostByCategory(1);
        int count = 0;
        for (Product product : products){
            if (count == 20) break;

            boolean isLiked = false;
            if (userId != null) {
                isLiked = likesService.existsByUserIdAndProductId(userId, product.getId());
            }
            Long reviewCount =reviewService.countByProductId(product.getId());
            ProductListResponseDto dto = new ProductListResponseDto(product,isLiked,reviewCount);
            dtos.add(dto);
            count++;
        }
        return dtos;
    }

    /** 키득pick 스위치로 검색  */
    public List<ProductListResponseDto> getProductListByswitch(String param, Long userId){
        if (param.equals("가성비")){
            return getRandomProductListForPick(productRepository.findByProductCategoryIdAndPriceLessThanEqual(1),4,userId);
        }else {
            return getRandomProductListForPick(productRepository.findByProductCategoryIdAndSwitchOptions_OptionName(1,param),4,userId);
        }
    }

    private List<ProductListResponseDto> getRandomProductListForPick(List<Product> products,int size, Long userId){
        List<ProductListResponseDto> dtos = new ArrayList<>();
        Random random = new Random();
        Set<Integer> usedIndices = new HashSet<>();

        while (dtos.size() != 4){
            int randomIndex = random.nextInt(products.size());
            if (!usedIndices.contains(randomIndex)){
                usedIndices.add(randomIndex);
                boolean isLiked = false;
                if (userId != null) {
                    isLiked = likesService.existsByUserIdAndProductId(userId, products.get(randomIndex).getId());
                }
                Long reviewCount =reviewService.countByProductId(products.get(randomIndex).getId());
                ProductListResponseDto dto = new ProductListResponseDto(products.get(randomIndex),isLiked,reviewCount);
                dtos.add(dto);
            }
        }
        log.info("size: {}",dtos.size());
        return dtos;
    }

    private OptionProductsResponseDto getRandomOptionProduct(int categoryId){
        Random random = new Random();
        List<Product> productsIdBy4 =  productRepository.findByProductCategoryId(categoryId);
        int randomIndex = random.nextInt(productsIdBy4.size());
        log.info("index={}",randomIndex);
        return new OptionProductsResponseDto(productsIdBy4.get(randomIndex));
    }

    public Product getProductById(Integer productId){
        return productRepository.findById(productId).orElseThrow(
                ()-> new NoSuchElementException("Product not found with id: " + productId)
        );
    }




}
