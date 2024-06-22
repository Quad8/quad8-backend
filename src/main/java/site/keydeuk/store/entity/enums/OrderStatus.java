package site.keydeuk.store.entity.enums;

public enum OrderStatus {
    READY,              // 준비 중
    PAYMENT_COMPLETED, // 결제 완료
    PREPARING,         // 배송 준비 중
    SHIPPING,          // 배송 중
    DELIVERED,         // 배송 완료
    CANCELED           // 주문 취소
}
