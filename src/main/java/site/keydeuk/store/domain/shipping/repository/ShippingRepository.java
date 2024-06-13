package site.keydeuk.store.domain.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.ShippingAddress;

public interface ShippingRepository extends JpaRepository<ShippingAddress, Long> {

}
