package site.keydeuk.store.domain.likes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.likes.dto.request.LikeDeleteRequest;
import site.keydeuk.store.domain.likes.dto.response.LikedProductsPage;
import site.keydeuk.store.domain.likes.dto.response.LikedProductsResponse;
import site.keydeuk.store.domain.likes.repository.LikesRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.Likes;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.User;

import java.util.List;

import static site.keydeuk.store.common.response.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Likes addLikes(Long userId, Integer productId) {
        if (likesRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new CustomException(ALREADY_EXIST_LIKE);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
        Likes like = Likes.builder()
                .user(user)
                .product(product)
                .build();

        return likesRepository.save(like);
    }

    @Transactional
    public void deleteLikes(Long userId, List<Long> request) {
        List<Likes> like = likesRepository.findByUserIdAndProductIdIn(userId, request)
                .orElseThrow(() -> new CustomException(LIKED_PRODUCTS_NOT_FOUND));

        likesRepository.deleteAll(like);
    }

    @Transactional(readOnly = true)
    public LikedProductsPage getLikedProductsPage(Long userId, Pageable pageable) {
        Page<Likes> likesPage = likesRepository.findByUserId(userId, pageable);
        List<Likes> likes = likesPage.getContent();
        List<Integer> productIds = likes.stream()
                .map(like -> like.getProduct().getId())
                .toList();
        List<Product> products = productRepository.findAllById(productIds);
        List<LikedProductsResponse> likedProductsResponses = products.stream()
                .map(product -> LikedProductsResponse.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .price(product.getPrice())
                        .productImg(
                                product.getProductImgs().isEmpty() ? null : product.getProductImgs().get(0).getImgUrl()
                        )
                        .build())
                .toList();
        return LikedProductsPage.builder()
                .likedProductsResponses(likedProductsResponses)
                .pageable(likesPage.getPageable())
                .build();
    }

    public boolean existsByUserIdAndProductId(Long userId, Integer productId){
        return likesRepository.existsByUserIdAndProductId(userId,productId);
    }
}
