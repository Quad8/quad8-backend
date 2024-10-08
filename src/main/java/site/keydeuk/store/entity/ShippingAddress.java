package site.keydeuk.store.entity;

import jakarta.persistence.*;
import lombok.*;
import site.keydeuk.store.common.entity.BaseTimeEntity;

@Entity
@Table(name = "shipping_address")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShippingAddress extends BaseTimeEntity {
    public static final ShippingAddress NULL = new ShippingAddress();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    private String name = "";
    private String zoneCode = "";
    private String address = "";
    private String detailAddress = "";
    private String phone = "";
    private Boolean isDefault = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void update(String name, String zoneCode, String address, String detailAddress, String phone, Boolean isDefault) {
        this.name = name;
        this.zoneCode = zoneCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.phone = phone;
        this.isDefault = isDefault;
    }
}
