package site.keydeuk.store.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.UserCoupon;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByUserIdAndCouponIsWelcomeTrue(Long userId);
    List<UserCoupon> findByUserId(Long userId);
}
