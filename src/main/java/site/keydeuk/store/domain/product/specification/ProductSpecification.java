package site.keydeuk.store.domain.product.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.ProductCategory;
import site.keydeuk.store.entity.ProductSwitchOption;

import java.util.Arrays;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> categoryEquals(String category) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductCategory> join = root.join("productCategory", JoinType.INNER);
            if ("etc".equals(category)){
                List<Integer> etcCategoryIds = Arrays.asList(4,5,6,7,8);
                return join.get("id").in(etcCategoryIds);
            }else {
                return criteriaBuilder.equal(join.get("name"), category);
            }
        };
    }

    public static Specification<Product> companyIn(List<String> companies) {
        return (root, query, criteriaBuilder) -> root.get("company").in(companies);
    }

    public static Specification<Product> switchOptionsIn(List<String> switchOptions) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductSwitchOption> join = root.join("switchOptions", JoinType.INNER);
            return criteriaBuilder.or(switchOptions.stream()
                    .map(option -> criteriaBuilder.like(join.get("optionName"),"%"+option+"%"))
                    .toArray(Predicate[]::new));
        };
    }

    public static Specification<Product> priceBetween(int minPrice, int maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }
}
