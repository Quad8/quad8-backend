package site.keydeuk.store.common.event.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import site.keydeuk.store.common.event.comment.CommentCreatedEvent;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.alarm.service.AlarmService;
import site.keydeuk.store.domain.community.repository.CommunityRepository;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.enums.NotificationType;

import static site.keydeuk.store.common.response.ErrorCode.POST_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class CommentEventListener {

    private final AlarmService alarmService;
    private final CommunityRepository communityRepository;

    @EventListener
    public void handleCommentCreatedEvent(CommentCreatedEvent event) {
        // 댓글 알림 처리
        Community community = communityRepository.findById(event.getCommunityId())
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        String content = event.getContent();
        alarmService.send(community.getUser(), NotificationType.COMMUNITY, content, event.getCommunityId());
    }
}
