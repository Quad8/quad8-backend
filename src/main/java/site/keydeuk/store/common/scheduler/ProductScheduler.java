package site.keydeuk.store.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.keydeuk.store.domain.product.service.ProductService;

@Component
@RequiredArgsConstructor
public class ProductScheduler {

    private final ProductService productService;

    @Scheduled(fixedDelay= 86400000) //24시간
    public void schedularFixedDelayTask(){
        productService.saveViewCount();
    }
}
