package site.keydeuk.store.domain.communitycomment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.community.repository.CommunityRepository;
import site.keydeuk.store.domain.communitycomment.dto.CommentResponseDto;
import site.keydeuk.store.domain.communitycomment.dto.create.CommentDto;
import site.keydeuk.store.domain.communitycomment.repository.CommunityCommentRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.CommunityComment;
import site.keydeuk.store.entity.User;

import java.util.List;
import java.util.stream.Collectors;

import static site.keydeuk.store.common.response.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CommunityCommentService {
    private final CommunityCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;


    /** 댓글 작성하기*/
    @Transactional
    public Long addComment(Long userId, Long communityId, CommentDto dto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(()-> new CustomException(POST_NOT_FOUND));
        CommunityComment comment = CommunityComment.builder()
                .user(user)
                .community(community)
                .content(dto.getContent())
                .build();
        commentRepository.save(comment);
        return comment.getId();
    }
    /** 댓글 삭제하기 */
    @Transactional
    public void deleteComment(Long userId, Long commentId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)) throw new CustomException(PERMISSION_DENIED);

        commentRepository.delete(comment);
    }

    /** 게시글에 작성된 댓글 조회 */
    public List<CommentResponseDto> getCommentListByPost(Long communityId){
        List<CommunityComment> comments = commentRepository.findByCommunity_Id(communityId);

        return comments.stream().map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /** 게시글의 작성된 댓글 수 */
    public Long getCommentCountByPost(Long communityId){
        return commentRepository.countByCommunity_Id(communityId);
    }

    @Transactional
    public void deleteAllCommentByCommunityId(Long communityId){
        commentRepository.deleteByCommunity_Id(communityId);
    }
}
