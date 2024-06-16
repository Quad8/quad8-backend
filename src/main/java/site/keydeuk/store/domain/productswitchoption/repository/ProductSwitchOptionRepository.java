package site.keydeuk.store.domain.productswitchoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.ProductSwitchOption;

@Repository
public interface ProductSwitchOptionRepository extends JpaRepository<ProductSwitchOption,Long> {

}
