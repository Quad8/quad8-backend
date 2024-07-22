package site.keydeuk.store.domain.alarm.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import site.keydeuk.store.entity.Notification;

import java.time.LocalDateTime;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AlarmDto {
    private Long id;

    private String message;

    private String type;

    private boolean isRead;

    private Long relatedId;

    private LocalDateTime createdAt;

    public AlarmDto(Notification notification){
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.type = notification.getNotificationType().toString();
        this.isRead = notification.isRead();
        this.relatedId = notification.getRelatedId();
        this.createdAt = notification.getCreatedAt();
    }

}
