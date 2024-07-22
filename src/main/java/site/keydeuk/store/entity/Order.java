package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.*;
import site.keydeuk.store.common.entity.BaseTimeEntity;
import site.keydeuk.store.entity.enums.OrderStatus;

import java.util.ArrayList;
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
    private String deliveryMessage;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Long totalPrice = 0L;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItems(List<OrderItem> orderItems) {
        this.orderItems.addAll(orderItems);
        this.totalPrice = this.orderItems.stream()
                .map(OrderItem::calculatePrice)
                .reduce(0L, Math::addExact);
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void updateShippingInfo(Long shippingAddressId, String deliveryMessage) {
        this.shippingAddressId = shippingAddressId;
        this.deliveryMessage = deliveryMessage;
    }
}