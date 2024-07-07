package site.keydeuk.store.domain.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.entity.Product;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

    private final ProductRepository productRepository;

    public List<Product> searchProducts(String search){
        return productRepository.findProductBySearch(search);
    }

}
