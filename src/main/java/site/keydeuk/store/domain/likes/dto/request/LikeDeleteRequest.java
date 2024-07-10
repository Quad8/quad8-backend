package site.keydeuk.store.domain.likes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "여러 개의 좋아요를 삭제하기 위한 DTO")
public class LikeDeleteRequest {
    @Schema(description = "삭제할 상품 ID 목록", example = "[1,2,3,4,5]")
    private List<Long> productIds;
}