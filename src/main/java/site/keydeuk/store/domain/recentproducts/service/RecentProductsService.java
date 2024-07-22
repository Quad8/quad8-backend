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

import java.util.ArrayList;
import java.util.List;
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

        zSetOperations.add(key, productId, System.currentTimeMillis());
        zSetOperations.removeRange(key, 0, -MAX_RECENT_PRODUCTS - 1);
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    public List<RecentProductDto> getRecentProducts(Long userId) {
        String key = RECENT_PRODUCTS_KEY_PREFIX + userId;
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Set<Object> productIds = zSetOperations.reverseRange(key, 0, MAX_RECENT_PRODUCTS - 1);

        List<RecentProductDto> recentProducts = new ArrayList<>();
        for (Object productIdObj : productIds) {
            Integer productId = (Integer) productIdObj;

            boolean isLiked = false;
            if (userId != null) isLiked = likesService.existsByUserIdAndProductId(userId,productId);
            Long reviewCount =reviewService.countByProductId(productId);
            RecentProductDto dto = new RecentProductDto(productService.getProductById(productId),isLiked,reviewCount);
            recentProducts.add(dto);
        }

        return recentProducts;
    }
}
