package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.keydeuk.store.common.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    private Long minPrice;
    private LocalDateTime expiredAt;
    private Boolean isWelcome;
    private Boolean isExpired;

    public void markAsExpired() {
        this.isExpired = true;
    }
}
