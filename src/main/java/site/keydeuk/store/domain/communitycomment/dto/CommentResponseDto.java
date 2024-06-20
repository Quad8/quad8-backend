package site.keydeuk.store.domain.communitycomment.dto;

import lombok.*;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.CommunityComment;

import java.time.LocalDateTime;

@Builder
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;

    private Long userId;

    private String nickName;

    private String imgUrl;

    private String content;

    private LocalDateTime createdAt;

    public static CommentResponseDto fromEntity(CommunityComment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .nickName(comment.getUser().getNickname())
                .imgUrl(comment.getUser().getImgUrl())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
