package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.*;
import site.keydeuk.store.common.entity.BaseTimeEntity;
import site.keydeuk.store.entity.enums.NotificationType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "notification")
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String message;

    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long relatedId;

}
