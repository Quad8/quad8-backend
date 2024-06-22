package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.*;
import site.keydeuk.store.common.entity.BaseTimeEntity;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String paymentOrderId;
    private Long shippingAddressId;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Integer totalPrice;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    public void addOrderItems(List<OrderItem> orderItems) {
        this.orderItems.addAll(orderItems);
        this.totalPrice = this.orderItems.stream()
                .map(OrderItem::calculatePrice)
                .reduce(0, Math::addExact);
    }
}