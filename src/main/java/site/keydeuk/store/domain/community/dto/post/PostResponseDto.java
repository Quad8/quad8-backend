package site.keydeuk.store.domain.community.dto.post;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import site.keydeuk.store.domain.communitycomment.dto.CommentResponseDto;
import site.keydeuk.store.domain.customoption.dto.custom.CustomDetailDto;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.CustomObject;
import site.keydeuk.store.entity.CustomOption;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostResponseDto {

    private Long id;

    private String title;

    private String content;

    private Long likeCount;

    private Long commentCount;

    private Long userId;

    private String nickName;

    private String userImage;

    private List<?> reviewImages;

    private CustomDetailDto custom;

    @JsonProperty("isLiked")
    private boolean isLiked;

    private List<CommentResponseDto> comments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PostResponseDto(Community community, CustomOption custom, CustomObject object){
        this.id = community.getId();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.likeCount = 0L;
        this.commentCount = 0L;
        this.userId = community.getUser().getId();
        this.nickName = community.getUser().getNickname();
        this.userImage = community.getUser().getImgUrl();
        this.reviewImages = community.getCommunityImg();
        this.custom = CustomDetailDto.fromEntity(custom,object);
        this.isLiked = false;
        this.comments = null;
        this.createdAt = community.getCreatedAt();
        this.updatedAt = community.getUpdatedAt();
    }
    public PostResponseDto(Community community, CustomOption custom){
        this.id = community.getId();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.likeCount = 0L;
        this.commentCount = 0L;
        this.userId = community.getUser().getId();
        this.nickName = community.getUser().getNickname();
        this.userImage = community.getUser().getImgUrl();
        this.reviewImages = community.getCommunityImg();
        this.custom = CustomDetailDto.fromEntity(custom);
        this.isLiked = false;
        this.comments = null;
        this.createdAt = community.getCreatedAt();
        this.updatedAt = community.getUpdatedAt();
    }

}
