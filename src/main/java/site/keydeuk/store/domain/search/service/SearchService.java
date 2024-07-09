package site.keydeuk.store.domain.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.review.service.ReviewService;
import site.keydeuk.store.domain.search.dto.ProductResponseDto;
import site.keydeuk.store.entity.Product;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

    private final ProductRepository productRepository;
    private final LikesService likesService;
    private final ReviewService reviewService;

    /** 상품명 전체 목록 조회 */
    public List<String> getProductNames(){
        return productRepository.findAllNames();
    }
    public Page<ProductResponseDto> searchProducts(String search, Pageable pageable,Long userId){
        Page<Product> products = productRepository.findProductBySearch(search,pageable);

        return products.map(product -> {
            boolean isLiked =false;
            if (userId!=null){
                isLiked = likesService.existsByUserIdAndProductId(userId, product.getId());
            }
            Long reviewCount = reviewService.countByProductId(product.getId());
            return new ProductResponseDto(product,isLiked,reviewCount);
        });
    }

}
