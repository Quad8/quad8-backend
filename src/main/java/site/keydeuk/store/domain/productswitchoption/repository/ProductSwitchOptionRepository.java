package site.keydeuk.store.domain.productswitchoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.ProductSwitchOption;

import java.util.List;

@Repository
public interface ProductSwitchOptionRepository extends JpaRepository<ProductSwitchOption,Long> {

    List<ProductSwitchOption> findByOptionName(String name);

}
