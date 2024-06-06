package site.keydeuk.store.domain.productimg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.ProductImg;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {

}
