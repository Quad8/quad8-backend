package site.keydeuk.store.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.coupon.dto.request.CreateCouponRequest;
import site.keydeuk.store.domain.coupon.dto.response.CouponResponse;
import site.keydeuk.store.domain.coupon.repository.CouponRepository;
import site.keydeuk.store.domain.coupon.repository.UserCouponRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.Coupon;
import site.keydeuk.store.entity.User;
import site.keydeuk.store.entity.UserCoupon;

import static site.keydeuk.store.common.response.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    @Transactional
    public CouponResponse createCoupon(Long userId, CreateCouponRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Coupon coupon = Coupon.builder()
                .name(request.name())
                .price(request.price())
                .minPrice(request.minPrice())
                .expiredAt(request.expiredDate())
                .build();
        Coupon savedCoupon = couponRepository.save(coupon);
        UserCoupon userCoupon = UserCoupon.builder()
                .user(user)
                .coupon(savedCoupon)
                .build();

        userCouponRepository.save(userCoupon);

        return CouponResponse.builder()
                .id(savedCoupon.getId())
                .name(savedCoupon.getName())
                .price(savedCoupon.getPrice())
                .minPrice(savedCoupon.getMinPrice())
                .expiredAt(savedCoupon.getExpiredAt())
                .build();
    }
}
