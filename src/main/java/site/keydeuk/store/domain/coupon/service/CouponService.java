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

import java.time.LocalDateTime;
import java.util.List;

import static site.keydeuk.store.common.response.ErrorCode.USER_ALREADY_HAS_WELCOME_COUPON;
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

        validateWelcomeCouponEligibility(userId, request);

        Coupon coupon = Coupon.builder()
                .name(request.name())
                .price(request.price())
                .minPrice(request.minPrice())
                .expiredAt(request.expiredDate())
                .isWelcome(request.isWelcome())
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

    @Transactional
    public List<CouponResponse> findCouponsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<UserCoupon> userCoupons = userCouponRepository.findByUserId(userId);

        removeExpiredCoupons(userCoupons);

        return userCoupons.stream()
                .map(userCoupon -> {
                    Coupon coupon = userCoupon.getCoupon();
                    return CouponResponse.builder()
                            .id(coupon.getId())
                            .name(coupon.getName())
                            .price(coupon.getPrice())
                            .minPrice(coupon.getMinPrice())
                            .expiredAt(coupon.getExpiredAt())
                            .build();
                })
                .toList();
    }

    private void removeExpiredCoupons(List<UserCoupon> userCoupons) {
        // 만료된 쿠폰 삭제
        userCoupons.removeIf(userCoupon -> {
            boolean isExpired = userCoupon.getCoupon().getExpiredAt().isBefore(LocalDateTime.now());
            if (isExpired) {
                userCouponRepository.delete(userCoupon);
            }
            return isExpired;
        });
    }

    private void validateWelcomeCouponEligibility(Long userId, CreateCouponRequest request) {
        if (request.isWelcome()) {
            boolean isWelcomeTrue = userCouponRepository.existsByUserIdAndCouponIsWelcomeTrue(userId);
            if (isWelcomeTrue) {
                throw new CustomException(USER_ALREADY_HAS_WELCOME_COUPON);
            }
        }
    }
}
