package site.keydeuk.store.domain.community.dto.list;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.CommunityImg;

import java.time.LocalDateTime;
import java.util.List;

import static site.keydeuk.store.common.response.ErrorCode.COMMON_RESOURCE_NOT_FOUND;

@Slf4j
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CommunityListResponseDto {

    private Long id;

    private String title;

    private int likeCount;

    private int commentCount;

    private String nickName;

    private String userImage;

    private String thumbnail;

    private boolean isLiked;

    private LocalDateTime updateAt;

    public CommunityListResponseDto(Community community){
        this.id = community.getId();
        this.title = community.getTitle();
        this.likeCount = 999;
        this.commentCount = 999;
        this.nickName = community.getUser().getNickname();
        this.userImage = community.getUser().getImgUrl();

        List<CommunityImg> imgs = community.getCommunityImg();
        if (!imgs.isEmpty()){
            this.thumbnail = community.getCommunityImg().get(0).getImgUrl();

        }else {
            log.info("community_{} is empty",community.getId());
            throw new CustomException(COMMON_RESOURCE_NOT_FOUND);
        }
        this.isLiked = false;
        this.updateAt = community.getUpdatedAt();

    }
}
