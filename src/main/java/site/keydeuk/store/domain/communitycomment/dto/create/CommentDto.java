package site.keydeuk.store.domain.communitycomment.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @NotNull
    @Schema(description = "댓글", example = "댓글을 작성해주세요.")
    private String content;

}
