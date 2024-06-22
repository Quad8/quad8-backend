package site.keydeuk.store.domain.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cancel {
    private BigDecimal cancelAmount;
    private String cancelReason;
    private BigDecimal taxFreeAmount;
    private long taxExemptionAmount;
    private BigDecimal refundableAmount;
    private BigDecimal easyPayDiscountAmount;
    private LocalDateTime canceledAt;
    private String transactionKey;
    private String receiptKey;
    private String cancelStatus;
    private String cancelRequestId;
}
