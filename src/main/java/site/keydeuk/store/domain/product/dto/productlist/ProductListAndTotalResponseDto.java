package site.keydeuk.store.domain.product.dto.productlist;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductListAndTotalResponseDto {

    private long totalCount;

    private Page<ProductListResponseDto> list;

}
