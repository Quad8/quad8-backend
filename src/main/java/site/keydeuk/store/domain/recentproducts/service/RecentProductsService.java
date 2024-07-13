package site.keydeuk.store.domain.recentproducts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.product.service.ProductService;
import site.keydeuk.store.domain.recentproducts.dto.RecentProductDto;
import site.keydeuk.store.domain.review.service.ReviewService;
import site.keydeuk.store.entity.Product;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RecentProductsService {

    private static final String RECENT_PRODUCTS_KEY_PREFIX = "recent:products:";
    private static final int MAX_RECENT_PRODUCTS = 8;

    private final RedisTemplate<String ,Object> redisTemplate;
    private final ProductService productService;
    private final LikesService likesService;
    private final ReviewService reviewService;

    public void addProduct(Long userId, Integer productId){
        String key = RECENT_PRODUCTS_KEY_PREFIX+userId;
        ZSetOperations<String,Object> zSetOperations = redisTemplate.opsForZSet();

        boolean isLiked = false;
        if (userId != null) isLiked = likesService.existsByUserIdAndProductId(userId,productId);
        Long reviewCount =reviewService.countByProductId(productId);

        //상품 정보 찾기
        RecentProductDto dto = new RecentProductDto(productService.getProductById(productId),isLiked,reviewCount);

        zSetOperations.add(key, dto, System.currentTimeMillis());
        zSetOperations.removeRange(key, 0, -MAX_RECENT_PRODUCTS - 1);
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    public Set<Object> getRecentProducts(Long userId) {
        String key = RECENT_PRODUCTS_KEY_PREFIX + userId;
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.reverseRange(key, 0, MAX_RECENT_PRODUCTS - 1);
    }
}
