package site.keydeuk.store.domain.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Product;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    Page<Product> findAllByOrderByViewsDesc(Pageable pageable);
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    @Query("select p from Product p LEFT join OrderItem oi on p.id = oi.productId group by p.id order by count (oi.id) DESC ")
    Page<Product> findAllOrderByOrder(Pageable pageable);
    @Query("select p from  Product p LEFT join OrderItem oi on p.id = oi.productId where p.productCategory.id = :categoryId group by p.id order by count (oi.id) DESC ")
    List<Product> findOrderByOrderedMostByCategory(Integer categoryId);

    List<Product> findByProductCategoryId(Integer categoryId);

    @Query("select p from Product p JOIN p.switchOptions s where s.optionName like %:optionName% and p.productCategory.id = :categoryId")
    List<Product> findByProductCategoryIdAndSwitchOptions_OptionName(int categoryId,String optionName);

    @Query("select p from Product p where p.price <= 100000 and p.productCategory.id = :categoryId")
    List<Product> findByProductCategoryIdAndPriceLessThanEqual(int categoryId);

    @Query("SELECT p, COUNT(oi.id) AS orderCount " +
            "FROM Product p " +
            "LEFT JOIN OrderItem oi ON p.id = oi.productId " +
            "LEFT JOIN p.productCategory pc " +
            "LEFT JOIN p.switchOptions so " +
            "WHERE pc.name = :category " +
            "AND (:companies IS NULL OR p.company IN :companies) " +
            "AND (:switchOptions IS NULL OR so.optionName IN :switchOptions) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "GROUP BY p.id " +
            "ORDER BY orderCount DESC")
    List<Product> findProductsOrderedByOrderCountAndFiltered(
            @Param("category") String category,
            @Param("companies") List<String> companies,
            @Param("switchOptions") List<String> switchOptions,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );

    @Query("SELECT p, COUNT(oi.id) AS orderCount " +
            "FROM Product p " +
            "LEFT JOIN OrderItem oi ON p.id = oi.productId " +
            "LEFT JOIN p.productCategory pc " +
            "LEFT JOIN p.switchOptions so " +
            "WHERE pc.id in (4,5,6,7,8)" +
            "AND (:companies IS NULL OR p.company IN :companies) " +
            "AND (:switchOptions IS NULL OR so.optionName IN :switchOptions) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "GROUP BY p.id " +
            "ORDER BY orderCount DESC")
    List<Product> findProductsByETCOrderedByOrderCountAndFiltered(
            @Param("companies") List<String> companies,
            @Param("switchOptions") List<String> switchOptions,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );
    @Query("select p from Product p where p.name like %:search%")
    List<Product> findProductBySearch(@Param("search") String search);

}
