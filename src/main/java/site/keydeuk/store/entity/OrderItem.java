package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer count;
    private Long switchOptionId;
    private Integer price;
    private Integer productId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Integer calculatePrice() {
        return count * price;
    }
}
