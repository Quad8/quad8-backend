package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.keydeuk.store.common.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "custom_option")
@EntityListeners(AuditingEntityListener.class)
public class CustomOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String layout;

    private String appearanceTexture;

    private String appearanceColor;

    private String baseKeyColor;

    @Column(name = "switch")
    private String keyboardSwitch;

    private boolean hasPointKey;

    private String pointKeyType;

    private String pointSetColor;

    private String imgUrl;

    private int price;
}
