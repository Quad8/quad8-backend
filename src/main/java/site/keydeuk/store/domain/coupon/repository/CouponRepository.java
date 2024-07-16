package site.keydeuk.store.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
