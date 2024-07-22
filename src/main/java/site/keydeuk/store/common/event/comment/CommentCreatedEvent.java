package site.keydeuk.store.common.event.comment;

import org.springframework.context.ApplicationEvent;

public class CommentCreatedEvent extends ApplicationEvent {
    private final Long communityId;
    private final String content;

    public CommentCreatedEvent(Object source, Long communityId, String content) {
        super(source);
        this.communityId = communityId;
        this.content = content;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public String getContent() {
        return content;
    }
}


