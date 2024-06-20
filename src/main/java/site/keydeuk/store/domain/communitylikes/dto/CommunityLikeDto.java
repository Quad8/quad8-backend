package site.keydeuk.store.domain.communitylikes.dto;


import lombok.Getter;
import lombok.Setter;
import site.keydeuk.store.entity.CommunityLikes;
@Setter
@Getter
public class CommunityLikeDto {
    private Long id;
    private Long userId;

    private Long communityId;

    public CommunityLikeDto(CommunityLikes likes){
        this.id = likes.getId();
        this.userId = likes.getUser().getId();
        this.communityId = likes.getCommunity().getId();
    }
}
