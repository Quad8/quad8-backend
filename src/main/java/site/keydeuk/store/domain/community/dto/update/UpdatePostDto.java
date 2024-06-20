package site.keydeuk.store.domain.community.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UpdatePostDto {

    @Schema(description = "제목", example = "제목을 수정합니다.")
    private String title;

    @Schema(description = "내용", example = "내용을 수정합니다.")
    private String  content;

    @Schema(description = "삭제될 이미지 index list", example = "9")
    private List<String> deletedFileList;

}
