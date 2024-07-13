package site.keydeuk.store.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
}
