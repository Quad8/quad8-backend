package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.*;
import site.keydeuk.store.common.entity.BaseTimeEntity;
import site.keydeuk.store.entity.enums.OrderStatus;

@Entity
@Table(name = "orders")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private boolean isCustom;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Long totalPrice;
    @OneToOne
    private Payment payment;
}