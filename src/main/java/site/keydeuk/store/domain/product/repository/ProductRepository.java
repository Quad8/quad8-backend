package site.keydeuk.store.domain.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.Product;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findAllByOrderByViewsDesc(Pageable pageable);
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    @Query("select p from Product p LEFT join OrderItem oi on p.id = oi.product.id group by p.id order by count (oi.id) DESC ")
    Page<Product> findAllOrderByOrder(Pageable pageable);
    @Query("select p from  Product p LEFT join OrderItem oi on p.id = oi.product.id where p.productCategory.id = :categoryId group by p.id order by count (oi.id) DESC ")
    List<Product> findOrderByOrderedMostByCategory(Integer categoryId);

    List<Product> findByProductCategoryId(Integer categoryId);

    Page<Product> findByProductCategoryId(Integer categoryId, Pageable pageable);
    @Query("select p from Product p JOIN p.switchOptions s where s.optionName like %:optionName% and p.productCategory.id = :categoryId")
    List<Product> findByProductCategoryIdAndSwitchOptions_OptionName(int categoryId,String optionName);

    @Query("select p from Product p where p.price <= 100000 and p.productCategory.id = :categoryId")
    List<Product> findByProductCategoryIdAndPriceLessThanEqual(int categoryId);

    @Query("select p from Product p where p.productCategory.id between :startCategoryId and :endCategoryId")
    Page<Product> findProductsByCategoryIdBetween(
            @Param("startCategoryId") Integer startId, @Param("endCategoryId") Integer endId, Pageable pageable);

    Page<Product> findByProductCategory_IdBetweenAndCompanyAndPriceBetween(
            Integer startCategoryId, Integer endCategoryId,
            String company, Integer minPrice, Integer maxPrice,
            Pageable pageable);


}
