package site.keydeuk.store.domain.likes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.ErrorCode;
import site.keydeuk.store.domain.likes.repository.LikesRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.Likes;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.User;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Likes addLikes(Long userId, Integer productId) {
        if (likesRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_LIKE);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        Likes like = Likes.builder()
                .user(user)
                .product(product)
                .build();

        return likesRepository.save(like);
    }
}
