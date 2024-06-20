package site.keydeuk.store.domain.community.dto.post;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Setter;
import site.keydeuk.store.domain.cartitem.dto.CartCustomResponseDto;
import site.keydeuk.store.domain.customoption.dto.custom.CustomDetailDto;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.CustomObject;
import site.keydeuk.store.entity.CustomOption;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostResponseDto {

    private Long id;

    private String title;

    private String content;

    private int likeCount;

    private int commentCount;

    private String nickName;

    private String userImage;

    private List<?> reviewImages;

    private CustomDetailDto custom;

    private boolean isLiked;

    private List<Integer> comments; // 재구현 필요

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PostResponseDto(Community community, CustomOption custom, CustomObject object){
        this.id = community.getId();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.likeCount = 0;
        this.commentCount = 0;
        this.nickName = community.getUser().getNickname();
        this.userImage = community.getUser().getImgUrl();
        this.reviewImages = community.getCommunityImg();
        this.custom = CustomDetailDto.fromEntity(custom,object);
        this.isLiked = false;
        this.comments = null;
        this.createdAt = community.getCreatedAt();
        this.updatedAt = community.getUpdatedAt();
    }

}
