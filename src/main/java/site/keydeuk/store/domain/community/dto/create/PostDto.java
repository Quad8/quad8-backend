package site.keydeuk.store.domain.community.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    @NotNull
    @Schema(description = "상품 id", example = "100027")
    private int productId;

    @NotNull
    @Schema(description = "제목", example = "제목을 작성해주세요.")
    private String title;

    @NotNull
    @Schema(description = "내용", example = "내용을 작성해주세요.")
    private String content;


}
