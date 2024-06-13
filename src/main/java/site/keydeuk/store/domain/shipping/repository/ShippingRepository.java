package site.keydeuk.store.domain.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import site.keydeuk.store.entity.ShippingAddress;

public interface ShippingRepository extends JpaRepository<ShippingAddress, Long> {
    @Modifying
    @Query("UPDATE ShippingAddress sa SET sa.isDefault = false WHERE sa.user.id = :userId AND sa.isDefault = true")
    void updateDefaultAddressToFalse(Long userId);
}
