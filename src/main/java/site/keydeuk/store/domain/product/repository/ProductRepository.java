package site.keydeuk.store.domain.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Product;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByProductCategoryId(Integer categoryId);

    Page<Product> findByProductCategoryId(Integer categoryId, Pageable pageable);

    @Query("select p from Product p where p.productCategory.id between :startCategoryId and :endCategoryId")
    Page<Product> findProductsByCategoryIdBetween(
            @Param("startCategoryId") Integer startId, @Param("endCategoryId") Integer endId, Pageable pageable);

    Page<Product> findByProductCategory_IdBetweenAndCompanyAndPriceBetween(
            Integer startCategoryId, Integer endCategoryId,
            String company, Integer minPrice, Integer maxPrice,
            Pageable pageable);


}
