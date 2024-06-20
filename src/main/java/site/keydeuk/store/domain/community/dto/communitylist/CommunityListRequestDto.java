package site.keydeuk.store.domain.community.dto.communitylist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityListRequestDto {

    @NotNull
    @Schema(description = "정렬(인기순 : popular, 조회순 : views, 최신순 : createdAt_desc)", example = "views_desc")
    private String sort;

    @Schema(description = "현재 페이지, default 0", example = "0")
    private int page;

    @Schema(description = "개수, default 10", example = "10")
    private int size;

    public CommunityListRequestDto(String sort){
        this.sort = sort;
        this.page = 0;
        this.size = 10;
    }

}
